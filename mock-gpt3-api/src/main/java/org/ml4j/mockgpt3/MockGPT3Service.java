/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.ml4j.mockgpt3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ml4j.gpt3.GPT3Choice;
import org.ml4j.gpt3.GPT3Request;
import org.ml4j.gpt3.GPT3Response;
import org.springframework.stereotype.Service;

/**
 * @author Michael Lavelle
 */
@Service
public class MockGPT3Service {
	
	public Map<Key, List<String>> outputsByPromptAndTemperature;
	
	public MockGPT3Service() throws FileNotFoundException, IOException {
		
		// Load the mock responses from the example files.
		Path resourcesDirectory = new File(MockGPT3Service.class.getClassLoader().getResource("").getFile()).toPath();
		Path experimentsDirectory = resourcesDirectory.getParent().getParent().getParent();
		File examplesDir = new File(experimentsDirectory.toFile(), "examples");
		this.outputsByPromptAndTemperature = new HashMap<>();
		for (File example : examplesDir.listFiles()) {
			if (example.isDirectory()) {
				// Obtain the contents of the prompt file.
				File promptFile = example.listFiles(file -> file.getPath().endsWith("prompt.txt"))[0];
				String prompt = new String(Files.readAllBytes(promptFile.toPath()));
				// For each output file, extract the temperature from the file name, and read the contents
				// of the file, splitting into multiple out strings.
				for (File file : example.listFiles(file -> file.getPath().contains("output_"))) {
					String temperatureString = file.getPath().substring(0, file.getPath().lastIndexOf("_") + 2);
					temperatureString = temperatureString.substring(temperatureString.length() - 3, 
							temperatureString.length()).replace('_', '.');
					BigDecimal temperature = new BigDecimal(temperatureString);
					String entireFileContents = new String(Files.readAllBytes(file.toPath()));
					entireFileContents = entireFileContents.replaceAll("\\*\\*", "");
					String[] parts = entireFileContents.split("---");
					for (String part : parts) {
						if (part.length() > prompt.length() + 1) {
							String output = part.substring(prompt.length());
							if (output.endsWith("\n")) {
								output = output.substring(0, output.length() - 2);
							}
							List<String> outputs = outputsByPromptAndTemperature.get(new Key(prompt, temperature));
							if (outputs == null) {
								outputs = new ArrayList<>();
								outputsByPromptAndTemperature.put(new Key(prompt, temperature), outputs);
								
							}
							outputs.add(output);
						}
					}
				}
			}
		}
	}

	public GPT3Response getResponse(GPT3Request request) {
		GPT3Response response = new GPT3Response();
		List<GPT3Choice> choicesInDescendingLikelyhoodOrder 
			= getChoicesInDescendingLikelyhoodOrder(request);
		if (request.getTemperature().floatValue() == 0) {
			response.setChoices(Arrays.asList(choicesInDescendingLikelyhoodOrder.get(0)));
		} else {
			int randomIndex = (int)Math.random() * choicesInDescendingLikelyhoodOrder.size();
			response.setChoices(Arrays.asList(choicesInDescendingLikelyhoodOrder.get(randomIndex)));
		}
		return response;
	}
	
	private List<GPT3Choice> getChoicesInDescendingLikelyhoodOrder(GPT3Request request) {
		if (request.getMaxTokens() != 512) {
			throw new UnsupportedOperationException("Outputs only currently mocked for max-tokens = 512");
		}
		GPT3Choice choice = new GPT3Choice();
		choice.setText(getMockText(request.getPrompt(), request.getTemperature()));
		return Arrays.asList(choice);
	}
	
	private String getMockText(String prompt, BigDecimal temperature) {
		List<String> mockResponses = outputsByPromptAndTemperature.get(new Key(prompt, temperature));
		if (mockResponses != null && !mockResponses.isEmpty()) {
			return mockResponses.get(0);
		} else {
			throw new UnsupportedOperationException("Prompt/Temperature not supported:" + prompt + ":" + temperature);
		}
	}
	
	/**
	 * A key we use to lookup mock output by prompt and temperature.
	 * 
	 * @author Michael Lavelle
	 */
	private class Key implements Serializable  {

		/**
		 * Default serialization id.
		 */
		private static final long serialVersionUID = 1L;
		private String prompt;
		private BigDecimal temperature;
		
		public Key(String prompt, BigDecimal temperature) {
			this.prompt = prompt;
			this.temperature = temperature;
			this.temperature.setScale(1);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((prompt == null) ? 0 : prompt.hashCode());
			result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (prompt == null) {
				if (other.prompt != null)
					return false;
			} else if (!prompt.equals(other.prompt))
				return false;
			if (temperature == null) {
				if (other.temperature != null)
					return false;
			} else if (!temperature.equals(other.temperature))
				return false;
			return true;
		}

		private MockGPT3Service getEnclosingInstance() {
			return MockGPT3Service.this;
		}
	}
}
