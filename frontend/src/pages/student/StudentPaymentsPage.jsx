import { useEffect, useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { getPaymentsByUserId } from "../../api/paymentApi";
import { getEnrollmentById } from "../../api/enrollmentApi";
import { getCourseById } from "../../api/courseApi";

function StudentPaymentsPage() {
    const { user } = useAuth();

    const [payments, setPayments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadPayments();
    }, []);

    const loadPayments = async () => {
        setLoading(true);
        setError("");

        try {
            const paymentData = await getPaymentsByUserId(user.id);

            const enrichedPayments = await Promise.all(
                paymentData.map(async (payment) => {
                    let enrollment = null;
                    let course = null;

                    try {
                        enrollment = await getEnrollmentById(
                            payment.enrollmentId
                        );

                        course = await getCourseById(
                            enrollment.courseId
                        );
                    } catch (error) {
                        console.error(
                            "Не удалось загрузить данные курса для платежа",
                            payment.id,
                            error
                        );
                    }

                    return {
                        ...payment,
                        enrollment,
                        course
                    };
                })
            );

            setPayments(enrichedPayments);
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось загрузить платежи"
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

    const formatAmount = (amount, currency) => {
        if (amount === null || amount === undefined) {
            return "—";
        }

        try {
            return new Intl.NumberFormat("ru-RU", {
                style: "currency",
                currency: currency || "RUB"
            }).format(amount);
        } catch {
            return `${amount} ${currency || ""}`;
        }
    };

    const getStatusText = (status) => {
        switch (status) {
            case "PENDING":
                return "Ожидает оплаты";

            case "COMPLETED":
                return "Оплачено";

            case "FAILED":
                return "Ошибка оплаты";

            case "REFUNDED":
                return "Возвращено";

            default:
                return status || "—";
        }
    };

    const getStatusClass = (status) => {
        switch (status) {
            case "COMPLETED":
                return "student-payment-status completed";

            case "FAILED":
                return "student-payment-status failed";

            case "REFUNDED":
                return "student-payment-status refunded";

            default:
                return "student-payment-status pending";
        }
    };

    if (loading) {
        return <div>Загрузка платежей...</div>;
    }

    return (
        <div>
            <div className="student-payments-header">
                <div>
                    <h1 className="page-title">
                        Платежи
                    </h1>

                    <p className="student-payments-subtitle">
                        История платежей за ваши курсы
                    </p>
                </div>

                <button
                    className="secondary-button"
                    type="button"
                    onClick={loadPayments}
                >
                    Обновить
                </button>
            </div>

            {error && (
                <div className="auth-error admin-message">
                    {error}
                </div>
            )}

            {!error && payments.length === 0 ? (
                <div className="dashboard-card">
                    У вас пока нет платежей.
                </div>
            ) : (
                <div className="student-payments-list">
                    {payments.map((payment) => (
                        <div
                            key={payment.id}
                            className="student-payment-card"
                        >
                            <div className="student-payment-card-header">
                                <div>
                                    <div className="student-payment-course-label">
                                        Курс
                                    </div>

                                    <h2 className="student-payment-course-title">
                                        {payment.course
                                            ? payment.course.title
                                            : "Курс недоступен"}
                                    </h2>

                                    {payment.course && (
                                        <div className="student-payment-teacher">
                                            Преподаватель:{" "}
                                            <strong>
                                                {payment.course.teacherName
                                                    || "Не назначен"}
                                            </strong>
                                        </div>
                                    )}
                                </div>

                                <span
                                    className={getStatusClass(
                                        payment.status
                                    )}
                                >
                                    {getStatusText(payment.status)}
                                </span>
                            </div>

                            <div className="student-payment-amount-block">
                                <span>
                                    Сумма
                                </span>

                                <strong>
                                    {formatAmount(
                                        payment.amount,
                                        payment.currency
                                    )}
                                </strong>
                            </div>

                            <div className="student-payment-divider" />

                            <div className="student-payment-info-grid">
                                <div className="student-payment-info-item">
                                    <span>
                                        Валюта
                                    </span>

                                    <strong>
                                        {payment.currency || "—"}
                                    </strong>
                                </div>

                                <div className="student-payment-info-item">
                                    <span>
                                        Статус записи
                                    </span>

                                    <strong>
                                        {payment.enrollment?.status || "—"}
                                    </strong>
                                </div>

                                <div className="student-payment-info-item">
                                    <span>
                                        Создан
                                    </span>

                                    <strong>
                                        {formatDate(
                                            payment.createdAt
                                        )}
                                    </strong>
                                </div>

                                <div className="student-payment-info-item">
                                    <span>
                                        Обновлён
                                    </span>

                                    <strong>
                                        {formatDate(
                                            payment.updatedAt
                                        )}
                                    </strong>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default StudentPaymentsPage;