import { NavLink } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

function Sidebar() {
    const { user } = useAuth();

    const studentMenu = [
        {
            title: "Главная",
            path: "/student"
        },
        {
            title: "Профиль",
            path: "/student/profile"
        },
        {
            title: "Курсы",
            path: "/student/courses"
        },
        {
            title: "Мои записи",
            path: "/student/enrollments"
        },
        {
            title: "Платежи",
            path: "/student/payments"
        },
        {
            title: "Документы",
            path: "/student/documents"
        }
    ];

    const teacherMenu = [
        {
            title: "Главная",
            path: "/teacher"
        },
        {
            title: "Профиль",
            path: "/teacher/profile"
        },
        {
            title: "Мои курсы",
            path: "/teacher/courses"
        }
    ];

    const adminMenu = [
        {
            title: "Главная",
            path: "/admin"
        },
        {
            title: "Пользователи",
            path: "/admin/users"
        },
        {
            title: "Курсы",
            path: "/admin/courses"
        },
        {
            title: "Записи на курсы",
            path: "/admin/enrollments"
        },
        {
            title: "Платежи",
            path: "/admin/payments"
        },
        {
            title: "Документы",
            path: "/admin/documents"
        }
    ];

    const getMenu = () => {
        switch (user.role) {
            case "ADMIN":
                return adminMenu;

            case "TEACHER":
                return teacherMenu;

            default:
                return studentMenu;
        }
    };

    return (
        <aside className="sidebar">
            <nav className="sidebar-nav">
                {getMenu().map((item) => (
                    <NavLink
                        key={item.path}
                        to={item.path}
                        end
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        {item.title}
                    </NavLink>
                ))}
            </nav>
        </aside>
    );
}

export default Sidebar;