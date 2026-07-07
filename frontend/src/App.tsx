import {useEffect, useState} from "react";

interface Question {
    session: string;
    question: string;
    answers: string[];
    questions_correct: number;
    question_number: number;
    total_questions: number;
}

interface AnswerResult {
    session: string;
    isCorrect: boolean;
    correctAnswer: string;
}

interface Stats {
    total_questions: number;
    questions_correct: number;
}

export default function App() {
    const [currentQuestion, setCurrentQuestion] = useState<Question>(null);
    const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null);
    const [result, setResult] = useState<AnswerResult | null>(null);
    const [loading, setLoading] = useState(false);

    const [difficulty, setDifficulty] = useState("medium");
    const [numberOfQuestions, setNumberOfQuestions] = useState(10);
    const [stats, setStats] = useState<Stats>(null);

    const [sessionId, setSessionId] = useState<string>(null);

    const startGame = async () => {
        setStats(null);
        let url: string;
        if (sessionId) {
            url = `/api/${sessionId}/createQuiz`;
        } else {
            url = "/api/createQuiz";
        }
        fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    amount: numberOfQuestions
                })
            }
        )
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                setCurrentQuestion({
                    session: data.session,
                    question: data.question,
                    answers: data.answers,
                    questions_correct: data.questions_correct,
                    question_number: data.question_number,
                    total_questions: data.total_questions
                });
                setSessionId(data.session);
            });
    }

    const getNewQuestion = () => {
        fetch(`/api/${sessionId}/getQuestion`, {
                method: "GET",
            }
        )
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                if (!data.question) {
                    setStats({
                        total_questions: data.total_questions,
                        questions_correct: data.questions_correct,
                    });
                } else {
                    setCurrentQuestion({
                        session: data.session,
                        question: data.question,
                        answers: data.answers,
                        questions_correct: data.questions_correct,
                        question_number: data.question_number,
                        total_questions: data.total_questions
                    });
                    setSessionId(data.session);
                }
            });
    }

    const submitAnswer = async (
        question: string,
        chosenAnswer: string
    ): Promise<AnswerResult> => {
        return new Promise((resolve) => {
            fetch(`/api/${sessionId}/checkAnswer`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    chosenAnswer: chosenAnswer
                })
            })
                .then((response) => {
                    return response.json();
                })
                .then((data) => {
                    console.log(data);
                    resolve({
                        isCorrect: data.is_correct,
                        correctAnswer: data.correct_answer
                    });
                });
        });
    };

    const handleAnswerClick = async (answer: string) => {
        if (selectedAnswer !== null || loading) return;

        setSelectedAnswer(answer);
        setLoading(true);

        try {
            const response = await submitAnswer(currentQuestion.question, answer);
            setResult(response);
        } finally {
            setLoading(false);
        }
    };

    const handleNext = () => {
        if (currentQuestion.question_number === currentQuestion.total_questions) {
            setCurrentQuestion(null);
        }

        getNewQuestion();
        setSelectedAnswer(null);
        setResult(null);
        setLoading(false);
    };

    const getButtonClass = (answer: string) => {
        if (!result) {
            if (selectedAnswer === answer) {
                return "btn btn-primary";
            }
            return "btn btn-outline-primary";
        }

        if (answer === result.correctAnswer) {
            return "btn btn-success";
        }
        if (
            answer === selectedAnswer
            && !result.isCorrect
        ) {
            return "btn btn-danger";
        }
        return "btn btn-outline-secondary";
    };

    return (
        <div className="container">
            <div className="row justify-content-center">
                <div className="col col-lg-8 col-xl-6 ">
                    <div className="mb-3">
                        <label htmlFor="questionRange" className="form-label">
                            Number of questions: {numberOfQuestions}
                        </label>
                        <input type="range" className="form-range" id="questionRange"
                               min={1} max={50} step={1}
                               value={numberOfQuestions}
                               onChange={(e) => setNumberOfQuestions(Number(e.target.value))}
                        />
                    </div>
                    <div className="input-group mb-3">
                        <div className="btn-group" role="group">
                            <input type="radio" className="btn-check" id="btnradio1" checked={difficulty === "easy"}
                                   onClick={() => setDifficulty("easy")} readOnly/>
                            <label className="btn btn-outline-primary" htmlFor="btnradio1">Easy</label>

                            <input type="radio" className="btn-check" id="btnradio2"
                                   checked={difficulty === "medium"}
                                   onClick={() => setDifficulty("medium")} readOnly/>
                            <label className="btn btn-outline-primary" htmlFor="btnradio2">Medium</label>

                            <input type="radio" className="btn-check" id="btnradio3"
                                   checked={difficulty === "hard"}
                                   onClick={() => setDifficulty("hard")} readOnly/>
                            <label className="btn btn-outline-primary" htmlFor="btnradio3">Hard</label>
                        </div>
                    </div>
                    <div>
                        <button type="button" className="btn btn-secondary" onClick={startGame}>
                            Start
                        </button>
                    </div>
                </div>
            </div>
            {currentQuestion && (
                <div className="row justify-content-center">
                    <div className="col col-lg-8 col-xl-6">
                        <div className="card">
                            <div className="card-header bg-secondary text-white text-center">
                                <h5>Question {currentQuestion.question_number} of {currentQuestion.total_questions}</h5>
                            </div>
                            <div className="card-body">
                                <h3 className="text-center">
                                    {currentQuestion.question}
                                </h3>
                                <div className="row">
                                    {currentQuestion.answers.map((answer, index) => (
                                        <div key={answer} className="col-12">
                                            <button
                                                type="button"
                                                disabled={loading || result !== null}
                                                onClick={() => handleAnswerClick(answer)}
                                                className={`${getButtonClass(answer)} w-100 mb-2`}
                                            >
                                                {loading && selectedAnswer === answer
                                                    ? ("Checking...")
                                                    : (answer)}
                                            </button>
                                        </div>
                                    ))}
                                </div>

                                {result && (
                                    <div>
                                        <button
                                            className="btn btn-secondary btn-lg"
                                            onClick={handleNext}
                                        >
                                            submit
                                        </button>
                                    </div>
                                )}

                            </div>

                        </div>

                    </div>
                </div>
            )}
            {stats && (
                <>
                    You had {stats.questions_correct} questions correct out of {stats.total_questions}
                </>
            )}
        </div>
    );
}