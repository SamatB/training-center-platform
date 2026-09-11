import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext";
import { getCoursesByTeacherId } from "../../api/courseApi";

function TeacherCoursesPage() {
    const { user } = useAuth();

    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        setLoading(true);
        setError("");

        try {
            const data = await getCoursesByTeacherId(user.id);

            setCourses(data);
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось загрузить ваши курсы"
            );
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

    if (loading) {
        return <div>Загрузка курсов...</div>;
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
                        Мои курсы
                    </h1>

                    <p className="admin-page-description">
                        Назначенных курсов: {courses.length}
                    </p>
                </div>

                <button
                    className="secondary-button"
                    type="button"
                    onClick={loadCourses}
                >
                    Обновить
                </button>
            </div>

            {courses.length === 0 ? (
                <div className="dashboard-card">
                    Вам пока не назначены курсы.
                </div>
            ) : (
                <div className="courses-grid">
                    {courses.map((course) => (
                        <div
                            key={course.id}
                            className="course-card"
                        >
                            <div className="teacher-course-header">
                                <h2>
                                    {course.title}
                                </h2>

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
                            </div>

                            <p className="course-description">
                                {course.description}
                            </p>

                            <div className="course-info">
                                <span>
                                    Длительность
                                </span>

                                <strong>
                                    {course.durationHours != null
                                        ? `${course.durationHours} ч.`
                                        : "—"}
                                </strong>
                            </div>

                            <div className="course-info">
                                <span>
                                    Стоимость
                                </span>

                                <strong>
                                    {formatPrice(course.price)}
                                </strong>
                            </div>

                            <Link
                                to={`/teacher/courses/${course.id}`}
                                className="course-details-link"
                            >
                                Открыть курс
                            </Link>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default TeacherCoursesPage;