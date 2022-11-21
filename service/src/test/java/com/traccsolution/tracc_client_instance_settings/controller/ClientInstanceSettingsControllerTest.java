package com.traccsolution.tracc_client_instance_settings.controller;

import com.traccsolution.tracc_client_instance_settings.configuration.ApplicationProperties;
import com.traccsolution.tracc_client_instance_settings.dto.child.ChildRequestDTO;
import com.traccsolution.tracc_client_instance_settings.dto.parent.ParentRequestDTO;
import com.traccsolution.tracc_client_instance_settings.extension.TestContainerExtension;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;
/**
 * @author KeeshanReddy
 * https://www.baeldung.com/rest-assured-tutorial
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({TestContainerExtension.class})
@Slf4j
class ParentControllerTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
    }


    @Test
    void whenRequestedPost_thenCreated() {
        ChildRequestDTO childRequestDTO = ChildRequestDTO
                .builder()
                .traccId(UUID.fromString("7de463cb-7e5c-4188-b007-89d2182f0310"))
                .stageId(UUID.fromString("7de463cb-7e5c-4188-b007-89d2182f0310"))
                .actionId(UUID.fromString("7de463cb-7e5c-4188-b007-89d2182f0310"))
                .build();

        ParentRequestDTO parent = ParentRequestDTO
                .builder()
                .name("some name")
                .description("some description")
                .organisationId(UUID.fromString("7de463cb-7e5c-4188-b007-89d2182f0310"))
                .child(childRequestDTO)
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .port(randomServerPort)
                .with()
                .body(parent)
                .post(getContextPath() + "/parent");
        response.then()
                .log().ifError()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }

    public String getContextPath() {
        return applicationProperties.getServletContextPath();
    }

}
