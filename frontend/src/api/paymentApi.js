import api from "./axios";

export const createPayment = async (data) => {
    const response = await api.post("/api/payments", data);
    return response.data;
};

export const getPayments = async () => {
    const response = await api.get("/api/payments");
    return response.data;
};

export const getPaymentsByUserId = async (userId) => {
    const response = await api.get(
        `/api/payments/user/${userId}`
    );

    return response.data;
};

export const getPaymentById = async (id) => {
    const response = await api.get(
        `/api/payments/${id}`
    );

    return response.data;
};