package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.util.DTOFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TraineeControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService traineeService;

    private final Trainee trainee = new Trainee();

    @Test
    void testGetProfile() throws Exception {
        TraineeSendDTO traineeSendDTO = mock(TraineeSendDTO.class);

        try (var mockDTOFactory = mockStatic(DTOFactory.class)) {
            mockDTOFactory.when(() -> DTOFactory.createTraineeSendDTO(trainee))
                    .thenReturn(traineeSendDTO);

            mockMvc.perform(get("/trainee/profile")
                            .with(user(trainee)))
                    .andExpect(status().isOk());
        }
    }

}
