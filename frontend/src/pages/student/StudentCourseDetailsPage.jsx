import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getCourseById } from "../../api/courseApi";
import { createEnrollment } from "../../api/enrollmentApi";
import { useAuth } from "../../auth/AuthContext";

function StudentCourseDetailsPage() {
    const { id } = useParams();
    const { user } = useAuth();

    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [enrolling, setEnrolling] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadCourse();
    }, [id]);

    const loadCourse = async () => {
        try {
            const data = await getCourseById(id);
            setCourse(data);
        } catch (error) {
            console.error(error);
            setError("Не удалось загрузить курс");
        } finally {
            setLoading(false);
        }
    };

    const handleEnroll = async () => {
        setEnrolling(true);
        setError("");
        setSuccess("");

        try {
            await createEnrollment({
                userId: user.id,
                courseId: course.id
            });

            setSuccess("Вы успешно записались на курс");
        } catch (error) {
            console.error(error);

            if (error.response?.data?.message) {
                setError(error.response.data.message);
            } else {
                setError("Не удалось записаться на курс");
            }
        } finally {
            setEnrolling(false);
        }
    };

    if (loading) {
        return <div>Загрузка курса...</div>;
    }

    if (!course) {
        return <div>Курс не найден</div>;
    }

    return (
        <div>
            <h1 className="page-title">
                {course.title}
            </h1>

            <div className="course-details-card">
                <p className="course-details-description">
                    {course.description}
                </p>

                <div className="profile-row">
                    <span className="profile-label">
                        Преподаватель
                    </span>

                    <span className="profile-value">
                        {course.teacherName || "Не назначен"}
                    </span>
                </div>

                <div className="profile-row">
                    <span className="profile-label">
                        Длительность
                    </span>

                    <span className="profile-value">
                        {course.durationHours} ч.
                    </span>
                </div>

                <div className="profile-row">
                    <span className="profile-label">
                        Стоимость
                    </span>

                    <span className="profile-value">
                        {course.price}
                    </span>
                </div>

                <div className="profile-row">
                    <span className="profile-label">
                        Статус
                    </span>

                    <span className="profile-value">
                        {course.active ? "Активен" : "Неактивен"}
                    </span>
                </div>

                {success && (
                    <div className="profile-success">
                        {success}
                    </div>
                )}

                {error && (
                    <div className="auth-error">
                        {error}
                    </div>
                )}

                <button
                    className="primary-button profile-button"
                    onClick={handleEnroll}
                    disabled={enrolling || !course.active}
                >
                    {enrolling
                        ? "Запись..."
                        : "Записаться на курс"}
                </button>
            </div>
        </div>
    );
}

export default StudentCourseDetailsPage;