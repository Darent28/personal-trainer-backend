package com.pt.personal_trainer.external.usda;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.pt.personal_trainer.config.UsdaProperties;
import com.pt.personal_trainer.domain.dto.UsdaSearchResponse;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.exception.CustomExceptions.ServerErrorException;

@Component
public class UsdaFoodClient {

    private final RestClient restClient;
    private final UsdaProperties usdaProperties;

    public UsdaFoodClient(RestClient.Builder builder, UsdaProperties usdaProperties) {
        this.usdaProperties = usdaProperties;
        this.restClient = builder.baseUrl(usdaProperties.getBaseUrl()).build();
    }

    public UsdaSearchResponse searchFoods(String query, int pageSize) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/foods/search")
                            .queryParam("query", query)
                            .queryParam("pageSize", Math.min(pageSize, 25))
                            .queryParam("api_key", usdaProperties.getApiKey())
                            .build())
                    .retrieve()
                    .onStatus(status -> status.value() == 400,
                            (req, res) -> { throw new ProcessServiceException("Invalid search query."); })
                    .onStatus(status -> status.value() == 403,
                            (req, res) -> { throw new ServerErrorException("USDA API key is invalid or missing."); })
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (req, res) -> { throw new ServerErrorException("USDA FoodData API returned an error: " + res.getStatusCode()); })
                    .body(UsdaSearchResponse.class);
        } catch (ProcessServiceException | ServerErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new ServerErrorException("Failed to reach USDA FoodData Central API.");
        }
    }
}
