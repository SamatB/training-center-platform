import api from "./axios";

export const register = async (data) => {
    const response = await api.post("/api/auth/register", data);
    return response.data;
};

export const login = async (data) => {
    const response = await api.post("/api/auth/login", data);
    return response.data;
};

export const getCurrentUser = async () => {
    const response = await api.get("/api/auth/me");
    return response.data;
};

export const updateCurrentUser = async (data) => {
    const response = await api.patch("/api/auth/me", data);
    return response.data;
};

export const getUserById = async (userId) => {
    const response = await api.get(
        `/api/auth/users/${userId}`
    );

    return response.data;
};