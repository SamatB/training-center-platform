import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { deleteCourse, getCourses } from "../../api/courseApi";

function AdminCoursesPage() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const [actionLoading, setActionLoading] = useState(null);
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        setLoading(true);
        setError("");

        try {
            const data = await getCourses();
            setCourses(data);
        } catch (error) {
            console.error(error);
            setError("Не удалось загрузить курсы");
        } finally {
            setLoading(false);
        }
    };

    const formatPrice = (price) => {
        if (price === null || price === undefined) {
            return "—";
        }

        return new Intl.NumberFormat("ru-RU", {
            style: "currency",
            currency: "RUB"
        }).format(price);
    };

    const handleDelete = async (course) => {
        const confirmed = window.confirm(
            `Удалить курс "${course.title}"?`
        );

        if (!confirmed) {
            return;
        }

        setActionLoading(course.id);
        setError("");
        setSuccess("");

        try {
            await deleteCourse(course.id);

            setSuccess("Курс удалён");

            await loadCourses();
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось удалить курс"
            );
        } finally {
            setActionLoading(null);
        }
    };

    if (loading) {
        return <div>Загрузка курсов...</div>;
    }

    return (
        <div>
            <div className="admin-page-header">
                <div>
                    <h1 className="page-title">
                        Курсы
                    </h1>

                    <p className="admin-page-description">
                        Всего курсов: {courses.length}
                    </p>
                </div>

                <button
                    className="primary-button"
                    type="button"
                    onClick={() => navigate("/admin/courses/create")}
                >
                    + Создать курс
                </button>
            </div>

            {error && (
                <div className="auth-error admin-message">
                    {error}
                </div>
            )}

            {success && (
                <div className="profile-success admin-message">
                    {success}
                </div>
            )}

            {!error && courses.length === 0 ? (
                <div className="dashboard-card">
                    Курсов пока нет.
                </div>
            ) : (
                <div className="admin-table-container">
                    <table className="admin-table">
                        <thead>
                        <tr>
                            <th>Название</th>
                            <th>Описание</th>
                            <th>Преподаватель</th>
                            <th>Длительность</th>
                            <th>Стоимость</th>
                            <th>Статус</th>
                            <th>Действия</th>
                        </tr>
                        </thead>

                        <tbody>
                        {courses.map((course) => (
                            <tr key={course.id}>
                                <td>
                                    <strong>
                                        {course.title}
                                    </strong>
                                </td>

                                <td className="course-description-cell">
                                    {course.description || "—"}
                                </td>

                                <td>
                                    {course.teacherName || "Не назначен"}
                                </td>

                                <td>
                                    {course.durationHours != null
                                        ? `${course.durationHours} ч.`
                                        : "—"}
                                </td>

                                <td>
                                    {formatPrice(course.price)}
                                </td>

                                <td>
                                        <span
                                            className={
                                                course.active
                                                    ? "user-status enabled"
                                                    : "user-status disabled"
                                            }
                                        >
                                            {course.active
                                                ? "Активен"
                                                : "Неактивен"}
                                        </span>
                                </td>

                                <td>
                                    <div className="admin-actions">
                                        <button
                                            className="course-edit-button"
                                            type="button"
                                            onClick={() =>
                                                navigate(`/admin/courses/${course.id}/edit`)
                                            }
                                        >
                                            Изменить
                                        </button>

                                        <button
                                            className="delete-action-button"
                                            type="button"
                                            disabled={actionLoading === course.id}
                                            onClick={() => handleDelete(course)}
                                        >
                                            {actionLoading === course.id
                                                ? "Удаление..."
                                                : "Удалить"}
                                        </button>
                                    </div>
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

export default AdminCoursesPage;