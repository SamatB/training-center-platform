import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {
    getCourseById,
    updateCourse
} from "../../api/courseApi";
import {getUsers} from "../../api/adminUserApi";

function AdminEditCoursePage() {
    const {id} = useParams();
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

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        loadData();
    }, [id]);

    const loadData = async () => {
        try {
            const [course, teacherData] = await Promise.all([
                getCourseById(id),
                getUsers({
                    page: 0,
                    size: 100,
                    role: "TEACHER",
                    sortBy: "firstName",
                    direction: "asc"
                })
            ]);

            setTeachers(teacherData.content || []);

            setFormData({
                title: course.title || "",
                description: course.description || "",
                teacherId: course.teacherId || "",
                durationHours: course.durationHours ?? "",
                price: course.price ?? "",
                active: Boolean(course.active)
            });
        } catch (error) {
            console.error(error);

            setError(
                "Не удалось загрузить данные курса"
            );
        } finally {
            setLoading(false);
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

        setSaving(true);
        setError("");

        try {
            const selectedTeacher = teachers.find(
                (teacher) => teacher.id === formData.teacherId
            );

            if (!selectedTeacher) {
                setError("Выберите преподавателя");
                setSaving(false);
                return;
            }

            await updateCourse(id, {
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
                || "Не удалось обновить курс"
            );
        } finally {
            setSaving(false);
        }
    };

    if (loading) {
        return <div>Загрузка курса...</div>;
    }

    return (
        <div>
            <h1 className="page-title">
                Редактировать курс
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
                            required
                            className="form-select"
                        >
                            <option value="">
                                Выберите преподавателя
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

                    {teachers.length === 0 && (
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
                                saving
                                || teachers.length === 0
                            }
                        >
                            {saving
                                ? "Сохранение..."
                                : "Сохранить"}
                        </button>

                        <button
                            type="button"
                            className="secondary-button"
                            disabled={saving}
                            onClick={() =>
                                navigate("/admin/courses")
                            }
                        >
                            Отмена
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default AdminEditCoursePage;