//package tr.com.obss.meetingmanager.meeting.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import tr.com.obss.meetingmanager.controller.MeetingController;
//import tr.com.obss.meetingmanager.dto.MeetingDTO;
//import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
//import tr.com.obss.meetingmanager.service.MeetingManagerService;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = MeetingController.class)
//class MeetingControllerTest {
//
//    @MockBean
//    private MeetHandlerFactory handlerFactory;
//    @MockBean
//    private MeetingManagerService meetingManagerService;
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
//        MeetingDTO meetingDTO =  MeetingDTO.builder()
//                .title("123")
//                .build();
//        String body = objectMapper.writeValueAsString(meetingDTO);
//
//        mvc.perform(post("/meetings/create")
//                .contentType("application/json")
//                .content(body))
//                .andExpect(status().isBadRequest());
//    }
//}
