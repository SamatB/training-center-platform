import { useAuth } from "../../auth/AuthContext";

function AdminHomePage() {
    const { user } = useAuth();

    return (
        <div>
            <h1 className="page-title">
                Панель администратора
            </h1>

            <div className="dashboard-card">
                <h2>
                    Добро пожаловать, {user.firstName}!
                </h2>

                <p>
                    Здесь будет управление пользователями,
                    курсами, записями, платежами и документами.
                </p>
            </div>
        </div>
    );
}

export default AdminHomePage;