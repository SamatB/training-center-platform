import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createCourse } from "../../api/courseApi";
import { getUsers } from "../../api/adminUserApi";

function AdminCreateCoursePage() {
    const navigate = useNavigate();

    const [teachers, setTeachers] = useState([]);

    const [formData, setFormData] = useState({
        title: "",
        description: "",
        teacherId: "",
        durationHours: "",
        price: "",
        active: true
    });

    const [loading, setLoading] = useState(false);
    const [teachersLoading, setTeachersLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadTeachers();
    }, []);

    const loadTeachers = async () => {
        try {
            const data = await getUsers({
                page: 0,
                size: 100,
                role: "TEACHER",
                sortBy: "firstName",
                direction: "asc"
            });

            setTeachers(data.content || []);
        } catch (error) {
            console.error(error);

            setError(
                "Не удалось загрузить список преподавателей"
            );
        } finally {
            setTeachersLoading(false);
        }
    };

    const handleChange = (event) => {
        const {
            name,
            value,
            type,
            checked
        } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox"
                ? checked
                : value
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        setLoading(true);
        setError("");

        try {
            const selectedTeacher = teachers.find(
                (teacher) => teacher.id === formData.teacherId
            );

            if (!selectedTeacher) {
                setError("Выберите преподавателя");
                setLoading(false);
                return;
            }

            await createCourse({
                title: formData.title,
                description: formData.description,
                teacherId: selectedTeacher.id,
                teacherName:
                    `${selectedTeacher.firstName} ${selectedTeacher.lastName}`,
                durationHours: Number(formData.durationHours),
                price: Number(formData.price),
                active: formData.active
            });

            navigate("/admin/courses");
        } catch (error) {
            console.error(error);

            setError(
                error.response?.data?.message
                || "Не удалось создать курс"
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1 className="page-title">
                Создать курс
            </h1>

            <div className="profile-card">
                <form
                    className="auth-form"
                    onSubmit={handleSubmit}
                >
                    <div className="form-group">
                        <label htmlFor="title">
                            Название
                        </label>

                        <input
                            id="title"
                            name="title"
                            type="text"
                            value={formData.title}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">
                            Описание
                        </label>

                        <textarea
                            id="description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            rows="5"
                            className="form-textarea"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="teacherId">
                            Преподаватель
                        </label>

                        <select
                            id="teacherId"
                            name="teacherId"
                            value={formData.teacherId}
                            onChange={handleChange}
                            disabled={teachersLoading}
                            required
                            className="form-select"
                        >
                            <option value="">
                                {teachersLoading
                                    ? "Загрузка преподавателей..."
                                    : "Выберите преподавателя"}
                            </option>

                            {teachers.map((teacher) => (
                                <option
                                    key={teacher.id}
                                    value={teacher.id}
                                >
                                    {teacher.firstName}{" "}
                                    {teacher.lastName}
                                    {" — "}
                                    {teacher.email}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="durationHours">
                            Длительность
                        </label>

                        <select
                            id="durationHours"
                            name="durationHours"
                            value={formData.durationHours}
                            onChange={handleChange}
                            required
                            className="form-select"
                        >
                            <option value="">
                                Выберите длительность
                            </option>

                            <option value="20">20 часов</option>
                            <option value="40">40 часов</option>
                            <option value="60">60 часов</option>
                            <option value="80">80 часов</option>
                            <option value="120">120 часов</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="price">
                            Стоимость
                        </label>

                        <select
                            id="price"
                            name="price"
                            value={formData.price}
                            onChange={handleChange}
                            required
                            className="form-select"
                        >
                            <option value="">
                                Выберите стоимость
                            </option>

                            <option value="5000">5 000 ₽</option>
                            <option value="10000">10 000 ₽</option>
                            <option value="15000">15 000 ₽</option>
                            <option value="20000">20 000 ₽</option>
                            <option value="30000">30 000 ₽</option>
                            <option value="50000">50 000 ₽</option>
                        </select>
                    </div>

                    <label className="checkbox-row">
                        <input
                            name="active"
                            type="checkbox"
                            checked={formData.active}
                            onChange={handleChange}
                        />

                        <span>
                            Курс активен
                        </span>
                    </label>

                    {teachers.length === 0 && !teachersLoading && (
                        <div className="auth-error">
                            В системе нет пользователей
                            с ролью TEACHER
                        </div>
                    )}

                    {error && (
                        <div className="auth-error">
                            {error}
                        </div>
                    )}

                    <div className="profile-actions">
                        <button
                            type="submit"
                            className="primary-button"
                            disabled={
                                loading
                                || teachersLoading
                                || teachers.length === 0
                            }
                        >
                            {loading
                                ? "Создание..."
                                : "Создать курс"}
                        </button>

                        <button
                            type="button"
                            className="secondary-button"
                            onClick={() =>
                                navigate("/admin/courses")
                            }
                            disabled={loading}
                        >
                            Отмена
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default AdminCreateCoursePage;