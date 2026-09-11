import { useEffect, useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { getDocumentsByUserId } from "../../api/documentApi";

function StudentDocumentsPage() {
    const { user } = useAuth();

    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadDocuments();
    }, []);

    const loadDocuments = async () => {
        try {
            const data = await getDocumentsByUserId(user.id);
            setDocuments(data);
        } catch (error) {
            console.error(error);
            setError("Не удалось загрузить документы");
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

    const getTypeText = (type) => {
        switch (type) {
            case "RECEIPT":
                return "Чек";

            case "INVOICE":
                return "Счёт";

            case "CERTIFICATE":
                return "Сертификат";

            default:
                return type;
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
                return status;
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

    if (error) {
        return (
            <div className="auth-error">
                {error}
            </div>
        );
    }

    return (
        <div>
            <h1 className="page-title">
                Документы
            </h1>

            {documents.length === 0 ? (
                <div className="dashboard-card">
                    У вас пока нет документов.
                </div>
            ) : (
                <div className="documents-list">
                    {documents.map((document) => (
                        <div
                            key={document.id}
                            className="document-card"
                        >
                            <div className="document-header">
                                <div>
                                    <h2>
                                        {getTypeText(document.type)}
                                    </h2>

                                    <p className="document-file-name">
                                        {document.fileName || "Файл ещё не создан"}
                                    </p>
                                </div>

                                <span
                                    className={getStatusClass(
                                        document.status
                                    )}
                                >
                                    {getStatusText(
                                        document.status
                                    )}
                                </span>
                            </div>

                            <div className="document-details">
                                <div className="document-detail">
                                    <span>
                                        Enrollment
                                    </span>

                                    <strong>
                                        {document.enrollmentId || "—"}
                                    </strong>
                                </div>

                                <div className="document-detail">
                                    <span>
                                        Payment
                                    </span>

                                    <strong>
                                        {document.paymentId || "—"}
                                    </strong>
                                </div>

                                <div className="document-detail">
                                    <span>
                                        Создан
                                    </span>

                                    <strong>
                                        {formatDate(
                                            document.createdAt
                                        )}
                                    </strong>
                                </div>

                                <div className="document-detail">
                                    <span>
                                        Обновлён
                                    </span>

                                    <strong>
                                        {formatDate(
                                            document.updatedAt
                                        )}
                                    </strong>
                                </div>

                                <div className="document-detail">
                                    <span>
                                        Storage key
                                    </span>

                                    <strong>
                                        {document.storageKey || "—"}
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

export default StudentDocumentsPage;