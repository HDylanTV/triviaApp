package nl.hdylantv;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

@Path("/api")
@ApplicationScoped
public class Api {

    @Inject
    Sessions sessions;

    @POST
    @Path("/createQuiz")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuiz(QuizConfig config) {
        Session session = sessions.createNewSession();
        session.generateNewQuiz(config);

        return session.createQuestionResponse();
    }

    @POST
    @Path("/{sessionId}/createQuiz")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuiz(@PathParam("sessionId") UUID sessionId, QuizConfig config) {
        Session session = sessions.fetch(sessionId);
        if (session == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                    "message", "unknown session"
                ))
                .build();
        }
        session.generateNewQuiz(config);

        return session.createQuestionResponse();
    }

    @GET
    @Path("/{sessionId}/getQuestion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion(@PathParam("sessionId") UUID sessionId) {
        Session session = sessions.fetch(sessionId);
        if (session == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                    "message", "unknown session"
                ))
                .build();
        }

        return session.createQuestionResponse();
    }

    @POST
    @Path("/{sessionId}/checkAnswer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAnswer(@PathParam("sessionId") UUID sessionId, CheckAnswer body) {
        Session session = sessions.fetch(sessionId);
        if (session == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                    "message", "unknown session"
                ))
                .build();
        }
        if (session.isCurrentQuestionAnswered()) {
            return Response.ok()
                .entity(Map.of(
                    "session", session.sessionId,
                    "message", "question already answered"
                ))
                .build();
        }
        String correctAnswer = session.getCorrectAnswer();

        session.submitAnswer(body.chosenAnswer());

        return Response.ok()
            .entity(Map.of(
                "session", session.sessionId,
                "is_correct", correctAnswer.equals(body.chosenAnswer()),
                "correct_answer", correctAnswer
            ))
            .build();
    }
}
