package com.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class JsonExampleVO {

    @JsonProperty("age")
    private Integer age;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private AddressDTO address;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("phoneNumbers")
    private List<String> phoneNumbers;

    @NoArgsConstructor
    @Data
    public static class AddressDTO {
        @JsonProperty("zip")
        private String zip;
        @JsonProperty("city")
        private String city;
        @JsonProperty("street")
        private String street;
    }
}
