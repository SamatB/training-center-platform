import { useAuth } from "../../auth/AuthContext";

function TeacherHomePage() {
    const { user } = useAuth();

    return (
        <div>
            <h1 className="page-title">
                Главная
            </h1>

            <div className="dashboard-card">
                <h2>
                    Добро пожаловать, {user.firstName}!
                </h2>

                <p>
                    Здесь будет информация о ваших курсах
                    и студентах.
                </p>
            </div>
        </div>
    );
}

export default TeacherHomePage;