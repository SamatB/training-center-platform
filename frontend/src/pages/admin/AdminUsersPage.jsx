import { useEffect, useState } from "react";
import {
    changeUserRole,
    changeUserStatus,
    deleteAllStudents,
    deleteUser,
    getUsers
} from "../../api/adminUserApi";
import { useAuth } from "../../auth/AuthContext";

function AdminUsersPage() {
    const { user: currentUser } = useAuth();

    const [users, setUsers] = useState([]);

    const [page, setPage] = useState(0);
    const [size] = useState(10);

    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [roleFilter, setRoleFilter] = useState("");

    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(null);

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadUsers();
    }, [page, roleFilter]);

    const loadUsers = async () => {
        setLoading(true);
        setError("");

        try {
            const data = await getUsers({
                page,
                size,
                role: roleFilter,
                sortBy: "createdAt",
                direction: "desc"
            });

            setUsers(data.content || []);
            setTotalPages(data.totalPages || 0);
            setTotalElements(data.totalElements || 0);
        } catch (error) {
            console.error(error);
            setError("Не удалось загрузить пользователей");
        } finally {
            setLoading(false);
        }
    };

    const handleRoleFilterChange = (event) => {
        setPage(0);
        setRoleFilter(event.target.value);
        setSuccess("");
    };

    const handleRoleChange = async (userId, role) => {
        setActionLoading(userId);
        setError("");
        setSuccess("");

        try {
            await changeUserRole(userId, role);

            setSuccess("Роль пользователя изменена");

            await loadUsers();
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось изменить роль"
            );
        } finally {
            setActionLoading(null);
        }
    };

    const handleStatusChange = async (user) => {
        const newStatus = !user.enabled;

        const actionText = newStatus
            ? "разблокировать"
            : "заблокировать";

        const confirmed = window.confirm(
            `Вы действительно хотите ${actionText} пользователя ${user.email}?`
        );

        if (!confirmed) {
            return;
        }

        setActionLoading(user.id);
        setError("");
        setSuccess("");

        try {
            await changeUserStatus(
                user.id,
                newStatus
            );

            setSuccess(
                newStatus
                    ? "Пользователь разблокирован"
                    : "Пользователь заблокирован"
            );

            await loadUsers();
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось изменить статус пользователя"
            );
        } finally {
            setActionLoading(null);
        }
    };

    const handleDelete = async (user) => {
        const confirmed = window.confirm(
            `Удалить пользователя ${user.firstName} ${user.lastName} (${user.email})?`
        );

        if (!confirmed) {
            return;
        }

        setActionLoading(user.id);
        setError("");
        setSuccess("");

        try {
            await deleteUser(user.id);

            setSuccess("Пользователь удалён");

            if (users.length === 1 && page > 0) {
                setPage((prev) => prev - 1);
            } else {
                await loadUsers();
            }
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось удалить пользователя"
            );
        } finally {
            setActionLoading(null);
        }
    };

    const handleDeleteAllStudents = async () => {
        const confirmed = window.confirm(
            "Удалить ВСЕ аккаунты с ролью STUDENT? Это действие нельзя отменить."
        );

        if (!confirmed) {
            return;
        }

        const secondConfirmation = window.confirm(
            "Подтвердите ещё раз: удалить всех STUDENT?"
        );

        if (!secondConfirmation) {
            return;
        }

        setActionLoading("delete-students");
        setError("");
        setSuccess("");

        try {
            const response = await deleteAllStudents();

            setSuccess(
                `Удалено пользователей STUDENT: ${response.deletedCount}`
            );

            setPage(0);

            await loadUsers();
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось удалить STUDENT"
            );
        } finally {
            setActionLoading(null);
        }
    };

    const formatDate = (value) => {
        if (!value) {
            return "—";
        }

        return new Date(value).toLocaleString("ru-RU");
    };

    const getRoleText = (role) => {
        switch (role) {
            case "ADMIN":
                return "Администратор";

            case "TEACHER":
                return "Преподаватель";

            case "STUDENT":
                return "Студент";

            default:
                return role;
        }
    };

    return (
        <div>
            <div className="admin-page-header">
                <div>
                    <h1 className="page-title">
                        Пользователи
                    </h1>

                    <p className="admin-page-description">
                        Всего пользователей: {totalElements}
                    </p>
                </div>

                <button
                    className="danger-button"
                    onClick={handleDeleteAllStudents}
                    disabled={
                        actionLoading === "delete-students"
                    }
                >
                    {actionLoading === "delete-students"
                        ? "Удаление..."
                        : "Удалить всех STUDENT"}
                </button>
            </div>

            <div className="admin-toolbar">
                <div className="admin-filter">
                    <label htmlFor="roleFilter">
                        Роль
                    </label>

                    <select
                        id="roleFilter"
                        value={roleFilter}
                        onChange={handleRoleFilterChange}
                    >
                        <option value="">
                            Все роли
                        </option>

                        <option value="STUDENT">
                            STUDENT
                        </option>

                        <option value="TEACHER">
                            TEACHER
                        </option>

                        <option value="ADMIN">
                            ADMIN
                        </option>
                    </select>
                </div>

                <button
                    className="secondary-button"
                    onClick={loadUsers}
                    disabled={loading}
                >
                    Обновить
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

            {loading ? (
                <div>
                    Загрузка пользователей...
                </div>
            ) : users.length === 0 ? (
                <div className="dashboard-card">
                    Пользователи не найдены.
                </div>
            ) : (
                <>
                    <div className="admin-table-container">
                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>Пользователь</th>
                                <th>Email</th>
                                <th>Роль</th>
                                <th>Статус</th>
                                <th>Создан</th>
                                <th>Действия</th>
                            </tr>
                            </thead>

                            <tbody>
                            {users.map((user) => {
                                const isCurrentUser =
                                    user.id === currentUser.id;

                                const isBusy =
                                    actionLoading === user.id;

                                return (
                                    <tr key={user.id}>
                                        <td>
                                            <div className="admin-user-name">
                                                {user.firstName}{" "}
                                                {user.lastName}
                                            </div>

                                            {isCurrentUser && (
                                                <span className="current-user-label">
                                                        Вы
                                                    </span>
                                            )}
                                        </td>

                                        <td>
                                            {user.email}
                                        </td>

                                        <td>
                                            <select
                                                className="role-select"
                                                value={user.role}
                                                disabled={
                                                    isBusy
                                                    || isCurrentUser
                                                }
                                                onChange={(event) =>
                                                    handleRoleChange(
                                                        user.id,
                                                        event.target.value
                                                    )
                                                }
                                            >
                                                <option value="STUDENT">
                                                    STUDENT
                                                </option>

                                                <option value="TEACHER">
                                                    TEACHER
                                                </option>

                                                <option value="ADMIN">
                                                    ADMIN
                                                </option>
                                            </select>

                                            <div className="role-description">
                                                {getRoleText(user.role)}
                                            </div>
                                        </td>

                                        <td>
                                                <span
                                                    className={
                                                        user.enabled
                                                            ? "user-status enabled"
                                                            : "user-status disabled"
                                                    }
                                                >
                                                    {user.enabled
                                                        ? "Активен"
                                                        : "Заблокирован"}
                                                </span>
                                        </td>

                                        <td>
                                            {formatDate(
                                                user.createdAt
                                            )}
                                        </td>

                                        <td>
                                            <div className="admin-actions">
                                                <button
                                                    className={
                                                        user.enabled
                                                            ? "warning-action-button"
                                                            : "success-action-button"
                                                    }
                                                    disabled={
                                                        isBusy
                                                        || isCurrentUser
                                                    }
                                                    onClick={() =>
                                                        handleStatusChange(
                                                            user
                                                        )
                                                    }
                                                >
                                                    {user.enabled
                                                        ? "Заблокировать"
                                                        : "Разблокировать"}
                                                </button>

                                                <button
                                                    className="delete-action-button"
                                                    disabled={
                                                        isBusy
                                                        || isCurrentUser
                                                    }
                                                    onClick={() =>
                                                        handleDelete(
                                                            user
                                                        )
                                                    }
                                                >
                                                    Удалить
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                );
                            })}
                            </tbody>
                        </table>
                    </div>

                    <div className="pagination">
                        <button
                            className="secondary-button"
                            disabled={page === 0}
                            onClick={() =>
                                setPage((prev) => prev - 1)
                            }
                        >
                            ← Назад
                        </button>

                        <span>
                            Страница{" "}
                            {totalPages === 0
                                ? 0
                                : page + 1}
                            {" "}
                            из{" "}
                            {totalPages}
                        </span>

                        <button
                            className="secondary-button"
                            disabled={
                                totalPages === 0
                                || page >= totalPages - 1
                            }
                            onClick={() =>
                                setPage((prev) => prev + 1)
                            }
                        >
                            Вперёд →
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

export default AdminUsersPage;