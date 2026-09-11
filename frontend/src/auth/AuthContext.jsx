import { createContext, useContext, useEffect, useState } from "react";
import {
    getCurrentUser,
    login as loginRequest,
    updateCurrentUser
} from "../api/authApi";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadCurrentUser();
    }, []);

    const loadCurrentUser = async () => {
        const token = localStorage.getItem("accessToken");

        if (!token) {
            setUser(null);
            setLoading(false);
            return;
        }

        try {
            const currentUser = await getCurrentUser();
            setUser(currentUser);
        } catch (error) {
            console.error(
                "Не удалось получить текущего пользователя",
                error
            );

            localStorage.removeItem("accessToken");
            setUser(null);
        } finally {
            setLoading(false);
        }
    };

    const login = async (email, password) => {
        const response = await loginRequest({
            email,
            password
        });

        localStorage.setItem(
            "accessToken",
            response.accessToken
        );

        const currentUser = await getCurrentUser();

        setUser(currentUser);

        return currentUser;
    };

    const updateProfile = async (data) => {
        const response = await updateCurrentUser(data);

        if (response.accessToken) {
            localStorage.setItem(
                "accessToken",
                response.accessToken
            );
        }

        setUser(response.user);

        return response.user;
    };

    const logout = () => {
        localStorage.removeItem("accessToken");
        setUser(null);
    };

    return (
        <AuthContext.Provider
            value={{
                user,
                loading,
                login,
                logout,
                updateProfile,
                isAuthenticated: Boolean(user)
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error(
            "useAuth должен использоваться внутри AuthProvider"
        );
    }

    return context;
}