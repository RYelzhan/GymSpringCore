package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainingControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingTypeService trainingTypeService;

    private final User user = new User();

    @Test
    void testGetTypes() throws Exception {
        List<TrainingTypeBasicDTO> mockTrainingTypes = new ArrayList<>();

        when(trainingTypeService.findAll())
                .thenReturn(mockTrainingTypes);

        mockMvc.perform(get("/trainings/types")
                        .with(user(user)))
                .andExpect(status().isOk());
    }
}
