import { useEffect, useState } from "react";
import { getDocuments } from "../../api/documentApi";
import { getEnrollmentById } from "../../api/enrollmentApi";
import { getCourseById } from "../../api/courseApi";
import { getUserById } from "../../api/adminUserApi";
import { getPaymentById } from "../../api/paymentApi";

function AdminDocumentsPage() {
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadDocuments();
    }, []);

    const loadDocuments = async () => {
        setLoading(true);
        setError("");

        try {
            const documentData = await getDocuments();

            const enrichedDocuments = await Promise.all(
                documentData.map(async (document) => {
                    let student = null;
                    let enrollment = null;
                    let course = null;
                    let payment = null;

                    const requests = [];

                    requests.push(
                        document.userId
                            ? getUserById(document.userId)
                            : Promise.reject()
                    );

                    requests.push(
                        document.enrollmentId
                            ? getEnrollmentById(document.enrollmentId)
                            : Promise.reject()
                    );

                    requests.push(
                        document.paymentId
                            ? getPaymentById(document.paymentId)
                            : Promise.reject()
                    );

                    const [
                        userResult,
                        enrollmentResult,
                        paymentResult
                    ] = await Promise.allSettled(requests);

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

                    if (paymentResult.status === "fulfilled") {
                        payment = paymentResult.value;
                    }

                    return {
                        ...document,
                        student,
                        enrollment,
                        course,
                        payment
                    };
                })
            );

            setDocuments(enrichedDocuments);
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось загрузить документы"
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

    const getTypeText = (type) => {
        switch (type) {
            case "RECEIPT":
                return "Чек";

            case "INVOICE":
                return "Счёт";

            case "CERTIFICATE":
                return "Сертификат";

            default:
                return type || "—";
        }
    };

    const getStatusText = (status) => {
        switch (status) {
            case "PENDING":
                return "Ожидает генерации";

            case "GENERATED":
                return "Сформирован";

            case "FAILED":
                return "Ошибка";

            default:
                return status || "—";
        }
    };

    const getStatusClass = (status) => {
        switch (status) {
            case "GENERATED":
                return "document-status generated";

            case "FAILED":
                return "document-status failed";

            default:
                return "document-status pending";
        }
    };

    if (loading) {
        return <div>Загрузка документов...</div>;
    }

    return (
        <div>
            <div className="admin-page-header">
                <div>
                    <h1 className="page-title">
                        Документы
                    </h1>

                    <p className="admin-page-description">
                        Всего документов: {documents.length}
                    </p>
                </div>

                <button
                    className="secondary-button"
                    type="button"
                    onClick={loadDocuments}
                >
                    Обновить
                </button>
            </div>

            {error && (
                <div className="auth-error admin-message">
                    {error}
                </div>
            )}

            {!error && documents.length === 0 ? (
                <div className="dashboard-card">
                    Документов пока нет.
                </div>
            ) : (
                !error && (
                    <div className="admin-table-container">
                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>Студент</th>
                                <th>Курс</th>
                                <th>Документ</th>
                                <th>Статус</th>
                                <th>Платёж</th>
                                <th>Создан</th>
                                <th>Document ID</th>
                            </tr>
                            </thead>

                            <tbody>
                            {documents.map((document) => (
                                <tr key={document.id}>
                                    <td>
                                        {document.student ? (
                                            <>
                                                <div className="admin-student-name">
                                                    {document.student.firstName}{" "}
                                                    {document.student.lastName}
                                                </div>

                                                <div className="admin-student-email">
                                                    {document.student.email}
                                                </div>

                                                <div className="admin-student-id">
                                                    {document.userId}
                                                </div>
                                            </>
                                        ) : (
                                            <>
                                                <div className="admin-student-name">
                                                    Пользователь недоступен
                                                </div>

                                                <div className="admin-student-id">
                                                    {document.userId || "—"}
                                                </div>
                                            </>
                                        )}
                                    </td>

                                    <td>
                                        <div className="admin-enrollment-course">
                                            {document.course
                                                ? document.course.title
                                                : "Курс недоступен"}
                                        </div>

                                        {document.enrollment && (
                                            <div className="admin-enrollment-course-id">
                                                {document.enrollment.courseId}
                                            </div>
                                        )}
                                    </td>

                                    <td>
                                        <div className="admin-document-type">
                                            {getTypeText(document.type)}
                                        </div>

                                        <div className="admin-document-file">
                                            {document.fileName
                                                || "Файл ещё не создан"}
                                        </div>
                                    </td>

                                    <td>
                                            <span
                                                className={getStatusClass(
                                                    document.status
                                                )}
                                            >
                                                {getStatusText(
                                                    document.status
                                                )}
                                            </span>
                                    </td>

                                    <td>
                                        {document.payment ? (
                                            <>
                                                <div className="admin-payment-amount">
                                                    {formatAmount(
                                                        document.payment.amount,
                                                        document.payment.currency
                                                    )}
                                                </div>

                                                <div className="admin-student-id">
                                                    {document.paymentId}
                                                </div>
                                            </>
                                        ) : (
                                            <div className="admin-muted-text">
                                                Платёж недоступен
                                            </div>
                                        )}
                                    </td>

                                    <td>
                                        {formatDate(document.createdAt)}
                                    </td>

                                    <td className="admin-id-cell">
                                        {document.id}
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

export default AdminDocumentsPage;