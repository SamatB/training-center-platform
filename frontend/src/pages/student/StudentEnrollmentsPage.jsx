import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext";
import { getEnrollmentsByUserId } from "../../api/enrollmentApi";
import { getCourseById } from "../../api/courseApi";
import { createPayment } from "../../api/paymentApi";

function StudentEnrollmentsPage() {
    const { user } = useAuth();
    const navigate = useNavigate();

    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [paymentLoading, setPaymentLoading] = useState(null);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadEnrollments();
    }, []);

    const loadEnrollments = async () => {
        setLoading(true);
        setError("");

        try {
            const enrollmentData =
                await getEnrollmentsByUserId(user.id);

            const enrichedEnrollments = await Promise.all(
                enrollmentData.map(async (enrollment) => {
                    try {
                        const course = await getCourseById(
                            enrollment.courseId
                        );

                        return {
                            ...enrollment,
                            course
                        };
                    } catch (error) {
                        console.error(
                            "Не удалось загрузить курс",
                            enrollment.courseId,
                            error
                        );

                        return {
                            ...enrollment,
                            course: null
                        };
                    }
                })
            );

            setEnrollments(enrichedEnrollments);
        } catch (error) {
            console.error(error);
            setError("Не удалось загрузить ваши записи");
        } finally {
            setLoading(false);
        }
    };

    const handlePayment = async (enrollment) => {
        if (!enrollment.course) {
            setError("Данные курса недоступны");
            return;
        }

        setPaymentLoading(enrollment.id);
        setError("");
        setSuccess("");

        try {
            await createPayment({
                enrollmentId: enrollment.id,
                userId: user.id,
                amount: enrollment.course.price,
                currency: "RUB"
            });

            setSuccess(
                `Платёж по курсу "${enrollment.course.title}" создан`
            );

            navigate("/student/payments");
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось создать платёж"
            );
        } finally {
            setPaymentLoading(null);
        }
    };

    const formatDate = (value) => {
        if (!value) {
            return "—";
        }

        return new Date(value).toLocaleString("ru-RU");
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

    const getStatusText = (status) => {
        switch (status) {
            case "ACTIVE":
                return "Активна";

            case "COMPLETED":
                return "Завершена";

            case "CANCELLED":
                return "Отменена";

            default:
                return status;
        }
    };

    if (loading) {
        return <div>Загрузка записей...</div>;
    }

    return (
        <div>
            <h1 className="page-title">
                Мои записи
            </h1>

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

            {enrollments.length === 0 ? (
                <div className="dashboard-card">
                    Вы пока не записаны ни на один курс.
                </div>
            ) : (
                <div className="enrollments-list">
                    {enrollments.map((enrollment) => (
                        <div
                            key={enrollment.id}
                            className="enrollment-card"
                        >
                            <div className="enrollment-header">
                                <div>
                                    <h2>
                                        {enrollment.course
                                            ? enrollment.course.title
                                            : "Курс недоступен"}
                                    </h2>

                                    {enrollment.course && (
                                        <p className="enrollment-description">
                                            {enrollment.course.description}
                                        </p>
                                    )}
                                </div>

                                <span
                                    className={
                                        enrollment.status === "ACTIVE"
                                            ? "status-badge active-status"
                                            : "status-badge"
                                    }
                                >
                                    {getStatusText(
                                        enrollment.status
                                    )}
                                </span>
                            </div>

                            <div className="enrollment-details">
                                <div className="enrollment-detail">
                                    <span>
                                        Преподаватель
                                    </span>

                                    <strong>
                                        {enrollment.course?.teacherName
                                            || "Не назначен"}
                                    </strong>
                                </div>

                                <div className="enrollment-detail">
                                    <span>
                                        Длительность
                                    </span>

                                    <strong>
                                        {enrollment.course
                                            ? `${enrollment.course.durationHours} ч.`
                                            : "—"}
                                    </strong>
                                </div>

                                <div className="enrollment-detail">
                                    <span>
                                        Стоимость
                                    </span>

                                    <strong>
                                        {enrollment.course
                                            ? formatPrice(
                                                enrollment.course.price
                                            )
                                            : "—"}
                                    </strong>
                                </div>

                                <div className="enrollment-detail">
                                    <span>
                                        Дата записи
                                    </span>

                                    <strong>
                                        {formatDate(
                                            enrollment.enrollmentDate
                                        )}
                                    </strong>
                                </div>
                            </div>

                            {enrollment.status === "ACTIVE"
                                && enrollment.course && (
                                    <div className="enrollment-actions">
                                        <button
                                            className="primary-button"
                                            type="button"
                                            disabled={
                                                paymentLoading
                                                === enrollment.id
                                            }
                                            onClick={() =>
                                                handlePayment(enrollment)
                                            }
                                        >
                                            {paymentLoading
                                            === enrollment.id
                                                ? "Создание платежа..."
                                                : `Оплатить ${formatPrice(
                                                    enrollment.course.price
                                                )}`}
                                        </button>
                                    </div>
                                )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default StudentEnrollmentsPage;