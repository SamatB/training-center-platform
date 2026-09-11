import { useState } from "react";
import { useAuth } from "../../auth/AuthContext";

function StudentProfilePage() {
    const { user, updateProfile } = useAuth();

    const [formData, setFormData] = useState({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email
    });

    const [editing, setEditing] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleEdit = () => {
        setFormData({
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email
        });

        setError("");
        setSuccess("");
        setEditing(true);
    };

    const handleCancel = () => {
        setFormData({
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email
        });

        setEditing(false);
        setError("");
        setSuccess("");
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        setLoading(true);
        setError("");
        setSuccess("");

        try {
            await updateProfile(formData);

            setSuccess("Профиль успешно обновлён");
            setEditing(false);
        } catch (error) {
            console.error(error);

            if (error.response?.data?.message) {
                setError(error.response.data.message);
            } else {
                setError("Не удалось обновить профиль");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1 className="page-title">
                Профиль
            </h1>

            <div className="profile-card">
                {!editing ? (
                    <>
                        <div className="profile-row">
                            <span className="profile-label">
                                Имя
                            </span>

                            <span className="profile-value">
                                {user.firstName}
                            </span>
                        </div>

                        <div className="profile-row">
                            <span className="profile-label">
                                Фамилия
                            </span>

                            <span className="profile-value">
                                {user.lastName}
                            </span>
                        </div>

                        <div className="profile-row">
                            <span className="profile-label">
                                Email
                            </span>

                            <span className="profile-value">
                                {user.email}
                            </span>
                        </div>

                        <div className="profile-row">
                            <span className="profile-label">
                                Роль
                            </span>

                            <span className="profile-value">
                                {user.role}
                            </span>
                        </div>

                        {success && (
                            <div className="profile-success">
                                {success}
                            </div>
                        )}

                        <button
                            className="primary-button profile-button"
                            onClick={handleEdit}
                        >
                            Редактировать
                        </button>
                    </>
                ) : (
                    <form
                        className="auth-form"
                        onSubmit={handleSubmit}
                    >
                        <div className="form-group">
                            <label htmlFor="firstName">
                                Имя
                            </label>

                            <input
                                id="firstName"
                                name="firstName"
                                type="text"
                                value={formData.firstName}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="lastName">
                                Фамилия
                            </label>

                            <input
                                id="lastName"
                                name="lastName"
                                type="text"
                                value={formData.lastName}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="email">
                                Email
                            </label>

                            <input
                                id="email"
                                name="email"
                                type="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        {error && (
                            <div className="auth-error">
                                {error}
                            </div>
                        )}

                        <div className="profile-actions">
                            <button
                                type="submit"
                                className="primary-button"
                                disabled={loading}
                            >
                                {loading
                                    ? "Сохранение..."
                                    : "Сохранить"}
                            </button>

                            <button
                                type="button"
                                className="secondary-button"
                                onClick={handleCancel}
                                disabled={loading}
                            >
                                Отмена
                            </button>
                        </div>
                    </form>
                )}
            </div>
        </div>
    );
}

export default StudentProfilePage;