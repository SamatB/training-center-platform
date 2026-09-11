import { useEffect, useState } from "react";
import { getPayments } from "../../api/paymentApi";
import { getEnrollmentById } from "../../api/enrollmentApi";
import { getCourseById } from "../../api/courseApi";
import { getUserById } from "../../api/adminUserApi";

function AdminPaymentsPage() {
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
            const paymentData = await getPayments();

            const enrichedPayments = await Promise.all(
                paymentData.map(async (payment) => {
                    let student = null;
                    let enrollment = null;
                    let course = null;

                    const [userResult, enrollmentResult] =
                        await Promise.allSettled([
                            getUserById(payment.userId),
                            getEnrollmentById(payment.enrollmentId)
                        ]);

                    if (userResult.status === "fulfilled") {
                        student = userResult.value;
                    }

                    if (enrollmentResult.status === "fulfilled") {
                        enrollment = enrollmentResult.value;

                        try {
                            course = await getCourseById(
                                enrollment.courseId
                            );
                        } catch (error) {
                            console.error(
                                "Не удалось загрузить курс",
                                enrollment.courseId,
                                error
                            );
                        }
                    }

                    return {
                        ...payment,
                        student,
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
                return "Оплачен";

            case "FAILED":
                return "Ошибка";

            case "REFUNDED":
                return "Возвращён";

            default:
                return status || "—";
        }
    };

    const getStatusClass = (status) => {
        switch (status) {
            case "COMPLETED":
                return "payment-status completed";

            case "FAILED":
                return "payment-status failed";

            case "REFUNDED":
                return "payment-status refunded";

            default:
                return "payment-status pending";
        }
    };

    if (loading) {
        return <div>Загрузка платежей...</div>;
    }

    return (
        <div>
            <div className="admin-page-header">
                <div>
                    <h1 className="page-title">
                        Платежи
                    </h1>

                    <p className="admin-page-description">
                        Всего платежей: {payments.length}
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
                    Платежей пока нет.
                </div>
            ) : (
                !error && (
                    <div className="admin-table-container">
                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>Студент</th>
                                <th>Курс</th>
                                <th>Сумма</th>
                                <th>Статус</th>
                                <th>Создан</th>
                                <th>Payment ID</th>
                            </tr>
                            </thead>

                            <tbody>
                            {payments.map((payment) => (
                                <tr key={payment.id}>
                                    <td>
                                        {payment.student ? (
                                            <>
                                                <div className="admin-student-name">
                                                    {
                                                        payment.student
                                                            .firstName
                                                    }{" "}
                                                    {
                                                        payment.student
                                                            .lastName
                                                    }
                                                </div>

                                                <div className="admin-student-email">
                                                    {
                                                        payment.student
                                                            .email
                                                    }
                                                </div>

                                                <div className="admin-student-id">
                                                    {payment.userId}
                                                </div>
                                            </>
                                        ) : (
                                            <>
                                                <div className="admin-student-name">
                                                    Пользователь недоступен
                                                </div>

                                                <div className="admin-student-id">
                                                    {payment.userId}
                                                </div>
                                            </>
                                        )}
                                    </td>

                                    <td>
                                        <div className="admin-enrollment-course">
                                            {payment.course
                                                ? payment.course.title
                                                : "Курс недоступен"}
                                        </div>

                                        {payment.enrollment && (
                                            <div className="admin-enrollment-course-id">
                                                {
                                                    payment.enrollment
                                                        .courseId
                                                }
                                            </div>
                                        )}
                                    </td>

                                    <td>
                                        <strong>
                                            {formatAmount(
                                                payment.amount,
                                                payment.currency
                                            )}
                                        </strong>
                                    </td>

                                    <td>
                                            <span
                                                className={getStatusClass(
                                                    payment.status
                                                )}
                                            >
                                                {getStatusText(
                                                    payment.status
                                                )}
                                            </span>
                                    </td>

                                    <td>
                                        {formatDate(
                                            payment.createdAt
                                        )}
                                    </td>

                                    <td className="admin-id-cell">
                                        {payment.id}
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

export default AdminPaymentsPage;