import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getCourses } from "../../api/courseApi";

function StudentCoursesPage() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
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

    if (loading) {
        return <div>Загрузка курсов...</div>;
    }

    if (error) {
        return <div className="auth-error">{error}</div>;
    }

    return (
        <div>
            <h1 className="page-title">
                Курсы
            </h1>

            {courses.length === 0 ? (
                <div className="dashboard-card">
                    Курсов пока нет.
                </div>
            ) : (
                <div className="courses-grid">
                    {courses.map((course) => (
                        <div
                            key={course.id}
                            className="course-card"
                        >
                            <h2>{course.title}</h2>

                            <p className="course-description">
                                {course.description}
                            </p>

                            <div className="course-info">
                                <span>
                                    Преподаватель:
                                </span>

                                <strong>
                                    {course.teacherName || "Не назначен"}
                                </strong>
                            </div>

                            <div className="course-info">
                                <span>
                                    Длительность:
                                </span>

                                <strong>
                                    {course.durationHours} ч.
                                </strong>
                            </div>

                            <div className="course-info">
                                <span>
                                    Стоимость:
                                </span>

                                <strong>
                                    {course.price}
                                </strong>
                            </div>

                            <div className="course-info">
                                <span>
                                    Статус:
                                </span>

                                <strong>
                                    {course.active
                                        ? "Активен"
                                        : "Неактивен"}
                                </strong>
                            </div>

                            <Link
                                to={`/student/courses/${course.id}`}
                                className="course-details-link"
                            >
                                Подробнее
                            </Link>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default StudentCoursesPage;