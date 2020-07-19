# gpt3-mock-api

I don't currently have access to the gpt3 API, however I was interested in exploring the request/response structure and wanted to run the gpt3-experiments.

This mock server has been written by reverse engineering the minimal request/response protocol required to run the gpt3-experiments with the default config.

Responses for temperature zero requests are deterministic - non-zero temperature requests return random results.

The mock server does not require headers to be set, and I'm assuming that responses only contain a subset of the actual response structure returned from the API.

I'd love an invite to be able to run these experiments for real.

# Running the mock server

Requires Java 8 or later.

To run the experiments against this mock server, change the urls in the openai_api.py file, replacing 'https' with 'http' and replacing 'api.openai.com' with 'localhost:8080'. 

The mock server can be run via the following command from this directory:


```
$ mvn spring-boot:run
