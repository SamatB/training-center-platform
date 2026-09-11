import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

function HomePage() {
    const { user } = useAuth();

    if (user.role === "ADMIN") {
        return <Navigate to="/admin" replace />;
    }

    if (user.role === "TEACHER") {
        return <Navigate to="/teacher" replace />;
    }

    return <Navigate to="/student" replace />;
}

export default HomePage;