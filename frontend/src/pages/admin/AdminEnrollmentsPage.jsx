import { useEffect, useState } from "react";
import { getEnrollments } from "../../api/enrollmentApi";
import { getCourseById } from "../../api/courseApi";
import { getUserById } from "../../api/adminUserApi";

function AdminEnrollmentsPage() {
    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadEnrollments();
    }, []);

    const loadEnrollments = async () => {
        setLoading(true);
        setError("");

        try {
            const enrollmentData = await getEnrollments();

            const enrichedEnrollments = await Promise.all(
                enrollmentData.map(async (enrollment) => {
                    const [courseResult, userResult] =
                        await Promise.allSettled([
                            getCourseById(enrollment.courseId),
                            getUserById(enrollment.userId)
                        ]);

                    return {
                        ...enrollment,
                        course:
                            courseResult.status === "fulfilled"
                                ? courseResult.value
                                : null,
                        student:
                            userResult.status === "fulfilled"
                                ? userResult.value
                                : null
                    };
                })
            );

            setEnrollments(enrichedEnrollments);
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось загрузить записи на курсы"
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
        return <div>Загрузка записей на курсы...</div>;
    }

    return (
        <div>
            <div className="admin-page-header">
                <div>
                    <h1 className="page-title">
                        Записи на курсы
                    </h1>

                    <p className="admin-page-description">
                        Всего записей: {enrollments.length}
                    </p>
                </div>

                <button
                    className="secondary-button"
                    type="button"
                    onClick={loadEnrollments}
                >
                    Обновить
                </button>
            </div>

            {error && (
                <div className="auth-error admin-message">
                    {error}
                </div>
            )}

            {!error && enrollments.length === 0 ? (
                <div className="dashboard-card">
                    Записей на курсы пока нет.
                </div>
            ) : (
                !error && (
                    <div className="admin-table-container">
                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>Студент</th>
                                <th>Курс</th>
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
                                                    {
                                                        enrollment.userId
                                                    }
                                                </div>
                                            </>
                                        ) : (
                                            <>
                                                <div className="admin-student-name">
                                                    Пользователь недоступен
                                                </div>

                                                <div className="admin-student-id">
                                                    {
                                                        enrollment.userId
                                                    }
                                                </div>
                                            </>
                                        )}
                                    </td>

                                    <td>
                                        <div className="admin-enrollment-course">
                                            {enrollment.course
                                                ? enrollment.course.title
                                                : "Курс недоступен"}
                                        </div>

                                        <div className="admin-enrollment-course-id">
                                            {enrollment.courseId}
                                        </div>
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
                )
            )}
        </div>
    );
}

export default AdminEnrollmentsPage;