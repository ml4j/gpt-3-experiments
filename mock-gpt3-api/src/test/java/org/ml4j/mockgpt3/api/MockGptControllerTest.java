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
package org.ml4j.mockgpt3.api;

import org.junit.jupiter.api.Test;
import org.ml4j.gpt3.GPT3Request;
import org.ml4j.gpt3.GPT3Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

/**
 * 
 * @author Michael Lavelle
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MockGptControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void onceUponATimeZeroTemparatureTestGivesCorrectResult() throws Exception {
		GPT3Request request = new GPT3Request();
		request.setPrompt("Once upon a time");
		request.setMaxTokens(512);
		request.setTemperature(BigDecimal.ZERO.setScale(1));
		ResponseEntity<GPT3Response> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/v1/engines/davinci/completions",request,
				GPT3Response.class);
		
		assertThat(responseEntity.getBody().getChoices().size()).isEqualTo(1);
		
		assertThat(responseEntity.getBody().getChoices().get(0).getText())
		.isEqualTo(", there was a little girl who was very sad. She was sad because she had no friends. She was sad because she had no one to play with. "
				+ "She was sad because she had no one to talk to. She was sad because she had no one to love.\n" + 
				"\n" + 
				"One day, the little girl was walking through the woods. She was walking through the woods because she was looking for a friend. "
				+ "She was walking through the woods because she was looking for someone to play with. She was walking through the woods because she was looking for someone to talk to. "
				+ "She was walking through the woods because she was looking for someone to love.\n" + 
				"\n" + 
				"As the little girl was walking through the woods, she came upon a little house. The little girl was very surprised to see a little house in the woods. "
				+ "She was very surprised to see a little house in the woods because she had never seen a little house in the woods before. She was very surprised to see a little house in the woods because "
				+ "she had never seen a little house in the woods before.\n" + 
				"\n" + 
				"The little girl walked up to the little house. She walked up to the little house because she was very curious. She walked up to the little house because she was very curious. "
				+ "She walked up to the little house because she was very curious. She walked up to the little house because she was very curious.\n" + 
				"\n" + 
				"The little girl knocked on the little house’s door. She knocked on the little house’s door because she was very polite. "
				+ "She knocked on the little house’s door because she was very polite. She knocked on the little house’s door because she was very polite. She knocked on the little house’s door because she was very polite.\n" + 
				"\n" + 
				"The little girl knocked on the little house’s door. She knocked on the little house’s door because she was very polite. "
				+ "She knocked on the little house’s door because she was very polite. She knocked on the little house’s door because she was very polite. She knocked on the little house’s door because she was very polite.\n" + 
				"\n" + 
				"The little girl knocked on the little house’s door. She knocked on the little house’s door because she was very polite. "
				+ "She knocked on the little house’s door because she was very polite. She knocked on the little house’s door because she was very polite. She knocked on the little house’s door because she was");
	}
	
	@Test
	public void onceUponATimeNonZeroTemparatureTestReturnsASingleResult() throws Exception {
		GPT3Request request = new GPT3Request();
		request.setPrompt("Once upon a time");
		request.setMaxTokens(512);
		request.setTemperature(new BigDecimal("0.7"));
		ResponseEntity<GPT3Response> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/v1/engines/davinci/completions",request,
				GPT3Response.class);
	

		assertThat(responseEntity.getBody().getChoices().size()).isEqualTo(1);
	
	}
}