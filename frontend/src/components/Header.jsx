import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

function Header() {
    const navigate = useNavigate();
    const { user, logout } = useAuth();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <header className="header">
            <div className="header-brand">
                Training Center Platform
            </div>

            <div className="header-user">
                <div className="header-user-info">
                    <span className="header-user-name">
                        {user.firstName} {user.lastName}
                    </span>

                    <span className="header-user-role">
                        {user.role}
                    </span>
                </div>

                <button
                    className="logout-button"
                    onClick={handleLogout}
                >
                    Выйти
                </button>
            </div>
        </header>
    );
}

export default Header;