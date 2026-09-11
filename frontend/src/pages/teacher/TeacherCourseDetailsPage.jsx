import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getCourseById } from "../../api/courseApi";
import { getEnrollmentsByCourseId } from "../../api/enrollmentApi";
import { getUserById } from "../../api/authApi";

function TeacherCourseDetailsPage() {
    const { id } = useParams();

    const [course, setCourse] = useState(null);
    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadData();
    }, [id]);

    const loadData = async () => {
        setLoading(true);
        setError("");

        try {
            const [courseData, enrollmentData] = await Promise.all([
                getCourseById(id),
                getEnrollmentsByCourseId(id)
            ]);

            const enrichedEnrollments = await Promise.all(
                enrollmentData.map(async (enrollment) => {
                    try {
                        const student = await getUserById(
                            enrollment.userId
                        );

                        return {
                            ...enrollment,
                            student
                        };
                    } catch (error) {
                        console.error(
                            "Не удалось загрузить пользователя",
                            enrollment.userId,
                            error
                        );

                        return {
                            ...enrollment,
                            student: null
                        };
                    }
                })
            );

            setCourse(courseData);
            setEnrollments(enrichedEnrollments);
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось загрузить данные курса"
            );
        } finally {
            setLoading(false);
        }
    };

    const formatDate = (value) => {
        if (!value) {
            return "—";
        }

        return new Date(value).toLocaleString("ru-RU");
    };

    const getStatusText = (status) => {
        switch (status) {
            case "ACTIVE":
                return "Активна";

            case "COMPLETED":
                return "Завершена";

            case "CANCELLED":
                return "Отменена";

            default:
                return status || "—";
        }
    };

    const getStatusClass = (status) => {
        switch (status) {
            case "ACTIVE":
                return "enrollment-admin-status active";

            case "COMPLETED":
                return "enrollment-admin-status completed";

            case "CANCELLED":
                return "enrollment-admin-status cancelled";

            default:
                return "enrollment-admin-status";
        }
    };

    if (loading) {
        return <div>Загрузка курса...</div>;
    }

    if (error) {
        return (
            <div className="auth-error">
                {error}
            </div>
        );
    }

    return (
        <div>
            <div className="admin-page-header">
                <div>
                    <h1 className="page-title">
                        {course?.title}
                    </h1>

                    <p className="admin-page-description">
                        Студентов на курсе: {enrollments.length}
                    </p>
                </div>

                <button
                    className="secondary-button"
                    type="button"
                    onClick={loadData}
                >
                    Обновить
                </button>
            </div>

            <div className="dashboard-card">
                <p>
                    <strong>Описание:</strong>{" "}
                    {course?.description}
                </p>

                <p>
                    <strong>Длительность:</strong>{" "}
                    {course?.durationHours} ч.
                </p>

                <p>
                    <strong>Стоимость:</strong>{" "}
                    {course?.price}
                </p>
            </div>

            <h2 style={{ marginTop: "30px" }}>
                Студенты курса
            </h2>

            {enrollments.length === 0 ? (
                <div className="dashboard-card">
                    На этот курс пока никто не записан.
                </div>
            ) : (
                <div className="admin-table-container">
                    <table className="admin-table">
                        <thead>
                        <tr>
                            <th>Студент</th>
                            <th>Статус</th>
                            <th>Дата записи</th>
                            <th>Enrollment ID</th>
                        </tr>
                        </thead>

                        <tbody>
                        {enrollments.map((enrollment) => (
                            <tr key={enrollment.id}>
                                <td>
                                    {enrollment.student ? (
                                        <>
                                            <div className="admin-student-name">
                                                {
                                                    enrollment.student
                                                        .firstName
                                                }{" "}
                                                {
                                                    enrollment.student
                                                        .lastName
                                                }
                                            </div>

                                            <div className="admin-student-email">
                                                {
                                                    enrollment.student
                                                        .email
                                                }
                                            </div>

                                            <div className="admin-student-id">
                                                {enrollment.userId}
                                            </div>
                                        </>
                                    ) : (
                                        <>
                                            <div className="admin-student-name">
                                                Пользователь недоступен
                                            </div>

                                            <div className="admin-student-id">
                                                {enrollment.userId}
                                            </div>
                                        </>
                                    )}
                                </td>

                                <td>
                                        <span
                                            className={getStatusClass(
                                                enrollment.status
                                            )}
                                        >
                                            {getStatusText(
                                                enrollment.status
                                            )}
                                        </span>
                                </td>

                                <td>
                                    {formatDate(
                                        enrollment.enrollmentDate
                                    )}
                                </td>

                                <td className="admin-id-cell">
                                    {enrollment.id}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

export default TeacherCourseDetailsPage;