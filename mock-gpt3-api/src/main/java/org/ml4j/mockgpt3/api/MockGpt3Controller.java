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

import org.ml4j.gpt3.GPT3Request;
import org.ml4j.gpt3.GPT3Response;
import org.ml4j.mockgpt3.MockGPT3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Lavelle
 */
@RestController
public class MockGpt3Controller {
	
	@Autowired
	private MockGPT3Service mockGPT3Service;
	
	@RequestMapping(value="/v1/engines/{model}/completions",method = RequestMethod.POST)
	public GPT3Response getResponse(@RequestBody GPT3Request request) {
		return mockGPT3Service.getResponse(request);
	}
}
