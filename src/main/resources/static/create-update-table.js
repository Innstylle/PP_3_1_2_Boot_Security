// Функция для получения данных пользователей с сервера
async function getAllUsers() {
    try {
        const response = await fetch('http://localhost:8080/admin/api/showUsersJson');
        if (!response.ok) {
            throw new Error('Ошибка загрузки данных о пользователях');
        }
        return await response.json();
    } catch (error) {
        console.error('Произошла ошибка:', error);
        return [];
    }
}
async function deleteUser(userId) {
    try {
        // Показываем модальное окно подтверждения перед удалением пользователя

        const response = await fetch(`http://localhost:8080/admin/api/showUser/${userId}`);

        // Проверяем успешность выполнения запроса
        if (!response.ok) {
            throw new Error('Ошибка при получении данных пользователя для удаления');
        }

        // Получаем данные пользователя из ответа

        const userData = await response.json();

        $('#deleteUserModal').modal('show');

        // Отображаем данные о пользователе в модальном окне
        $("#deleteUserId").text(userData.id);
        $("#deleteUserFirstName").text(userData.firstName);
        $("#deleteUserLastName").text(userData.lastName);
        $("#deleteUserAge").text(userData.age);
        $("#deleteUserEmail").text(userData.email);

        // Добавляем обработчик события на кнопку подтверждения удаления
        $("#confirmDeleteButton").on("click", async function() {
            // Отправляем запрос на удаление пользователя с указанным ID
            const response = await fetch(`http://localhost:8080/admin/api/removeUser/${userId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                }
            });

            // Проверяем успешность выполнения запроса
            if (response.ok) {
                // Если пользователь успешно удален, обновляем таблицу
                await createUpdateTable();
                console.log('Пользователь успешно удален.');
            } else {
                console.error('Ошибка удаления пользователя:', response.statusText);
            }

            // Закрываем модальное окно подтверждения удаления
            $('#deleteUserModal').modal('hide');

            // Удаляем обработчик события на кнопку подтверждения удаления
            $("#confirmDeleteButton").off("click");
        });
    } catch (error) {
        console.error('Произошла ошибка при удалении пользователя:', error);
    }
}



async function openEditModal(userId) {
    try {
        // Отправляем запрос на получение данных пользователя для редактирования
        const response = await fetch(`http://localhost:8080/admin/api/showUser/${userId}`);

        // Проверяем успешность выполнения запроса
        if (!response.ok) {
            throw new Error('Ошибка при получении данных пользователя для редактирования');
        }

        // Получаем данные пользователя из ответа
        const userData = await response.json();

        // Заполняем форму данными пользователя
        $("#editModal input[name='id']").val(userData.id);
        $("#editModal input[name='firstName']").val(userData.firstName);
        $("#editModal input[name='lastName']").val(userData.lastName);
        $("#editModal input[name='age']").val(userData.age);
        $("#editModal input[name='email']").val(userData.email);
        $("#editModal input[name='password']").val(userData.password);

        // Получаем данные о всех ролях с сервера
        const rolesResponse = await fetch('http://localhost:8080/admin/api/allRoles');
        if (!rolesResponse.ok) {
            throw new Error('Ошибка загрузки данных о ролях');
        }
        const rolesData = await rolesResponse.json();

        // Очищаем контейнер с ролями в модальном окне
        $('#edit-roles').empty();

        // Добавляем чекбоксы для каждой роли
        rolesData.forEach(role => {
            let checkbox = $(`<input type="checkbox" name="roles" value="${role.id}"> ${role.name}<br>`);
            /*if (userData.roleIds.includes(role.id)) {
                checkbox.prop('checked', true); // Устанавливаем чекбокс, если роль уже присутствует у пользователя
            }*/
            $('#edit-roles').append(checkbox);
        });

        // Открываем модальное окно для редактирования пользователя
        $("#editModal").modal("show");
    } catch (error) {
        console.error('Произошла ошибка:', error);
    }

    // Добавляем обработчик события отправки формы редактирования
    $("#formEdit").submit(async function(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение формы (перезагрузку страницы)

        // Получаем данные из формы
        let formData = {
            id: $("#editModal input[name='id']").val(),
            firstName: $("#editModal input[name='firstName']").val(),
            lastName: $("#editModal input[name='lastName']").val(),
            age: $("#editModal input[name='age']").val(),
            email: $("#editModal input[name='email']").val(),
            password: $("#editModal input[name='password']").val(),
            roleIds: [] // Инициализируем массив ролей
        };

        // Собираем выбранные роли
        $("#editModal input[name='roles']:checked").each(function() {
            formData.roleIds.push($(this).val());
        });

        // Отправляем данные на сервер для редактирования пользователя
        try {
            const response = await fetch(`http://localhost:8080/admin/api/editUser/${formData.id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(formData)
            });

            // Проверяем успешность выполнения запроса
            if (response.ok) {
                // Если пользователь успешно отредактирован, обновляем таблицу
                await createUpdateTable();
                console.log('Пользователь успешно отредактирован.');
            } else {
                console.error('Ошибка редактирования пользователя:', response.statusText);
            }
        } catch (error) {
            console.error('Произошла ошибка при редактировании пользователя:', error);
        }

        // Закрываем модальное окно после редактирования
        $("#editModal").modal("hide");
    });
}


// Создаем таблицу с данными пользователей
async function createUpdateTable() {
    return new Promise(async (resolve, reject) => {
        try {
            // Получаем данные о пользователях с сервера
            const all_user = await getAllUsers();

            $("#insertTableHere").empty();

            // Создаем таблицу
            let table = $("<table>").addClass("table table-hover");

            // Создаем заголовок таблицы
            let thead = $("<thead>");
            let tr = $("<tr>");

            // Добавляем заголовки колонок
            let columns = ["Id", "FirstName", "LastName", "Age", "Email", "Edit", "Delete"];
            columns.forEach(function (columnName) {
                let th = $("<th>").attr("scope", "col").text(columnName);
                tr.append(th);
            });

            // Добавляем строку заголовков в thead
            thead.append(tr);

            // Добавляем thead в таблицу
            table.append(thead);

            // Создаем тело таблицы
            let tbody = $("<tbody>");

            // Добавляем строки с данными пользователей
            all_user.forEach(function (user) {
                let tr = $("<tr>");

                tr.append($("<td>").text(user.id));
                tr.append($("<td>").text(user.firstName));
                tr.append($("<td>").text(user.lastName));
                tr.append($("<td>").text(user.age));
                tr.append($("<td>").text(user.email));

                // Создаем кнопку "Редактировать" и добавляем обработчик события click


                let editButton = $("<button>")
                    .addClass("btn btn-primary")
                    .text("Edit")
                    .on("click", function() {
                        openEditModal(user.id);
                    });


                let deleteButton = $("<button>")
                    .addClass("btn btn-danger")
                    .text("Delete")
                    .on("click", function() {
                        // При нажатии на кнопку "Удалить" вызываем функцию для удаления пользователя
                        deleteUser(user.id,);
                    });




                let deleteModal = $("<div>")
                    .addClass("modal fade")
                    .attr("id", "deleteModal_" + user.id)
                    .attr("tabindex", "-1")
                    .attr("aria-labelledby", "deleteModalLabel")
                    .attr("aria-hidden", "true")

                let editCell = $("<td>").append(editButton);
                let deleteCell = $("<td>").append(deleteButton, deleteModal);

                tr.append(editCell, deleteCell);

                tbody.append(tr);
            });

            // Добавляем tbody в таблицу
            table.append(tbody);

            // Вставляем таблицу в указанное место
            $("#insertTableHere").append(table);

            // Резолвим промис после создания таблицы
            resolve();
        } catch (error) {
            // Реджектим промис в случае ошибки
            reject(error);
        }
    });
}

// Вызываем функцию для создания таблицы и обрабатываем ее завершение с помощью .then() и .catch()
createUpdateTable().then(() => {
    console.log('Таблица успешно создана!');
}).catch((error) => {
    console.error('Ошибка при создании таблицы:', error);
});

// В функции showUserForm() заменим список выбора на чекбоксы
async function showUserForm() {
    // Очищаем контейнер, если в нем уже есть содержимое
    $("#insertTableHere").empty();

    // Создаем форму для добавления нового пользователя
    let form = $("<form>").attr({
        "id": "userForm",
        "method": "post", // Устанавливаем метод отправки формы как post
    });

    // Добавляем поля для ввода данных пользователя
    form.append("<label>First Name:</label><br>");
    form.append("<input type='text' name='firstName'><br><br>");

    form.append("<label>Last Name:</label><br>");
    form.append("<input type='text' name='lastName'><br><br>");

    form.append("<label>Age:</label><br>");
    form.append("<input type='text' name='age'><br><br>");

    form.append("<label>Email:</label><br>");
    form.append("<input type='email' name='email'><br><br>");

    form.append("<label>Password:</label><br>");
    form.append("<input type='password' name='password'><br><br>");

    // Получаем данные о ролях с сервера
    try {
        const rolesResponse = await fetch('http://localhost:8080/admin/api/allRoles');
        if (!rolesResponse.ok) {
            throw new Error('Ошибка загрузки данных о ролях');
        }
        const rolesData = await rolesResponse.json();

        // Добавляем чекбоксы для ролей
        form.append("<label>Roles:</label><br>");
        rolesData.forEach(role => {
            form.append(`<input type='checkbox' name='roles' value='${role.id}'> ${role.name}<br>`);
        });
    } catch (error) {
        console.error('Произошла ошибка при загрузке данных о ролях:', error);
    }

    // Добавляем кнопку для отправки формы
    form.append("<button type='submit'>Submit</button>");

    // Добавляем форму в контейнер
    $("#insertTableHere").append(form);

    $('#userForm').submit(handleSubmit);
}

// В функции handleSubmit() преобразуем выбранные чекбоксы в массив ролей перед отправкой на сервер
async function handleSubmit(event) {
    event.preventDefault(); // Предотвращаем стандартное поведение формы (перезагрузку страницы)

    // Получаем данные из формы
    let formData = {
        firstName: $("#userForm input[name='firstName']").val(),
        lastName: $("#userForm input[name='lastName']").val(),
        age: $("#userForm input[name='age']").val(),
        email: $("#userForm input[name='email']").val(),
        password: $("#userForm input[name='password']").val(),
        roleIds: [] // Инициализируем массив ролей
    };

    // Собираем выбранные роли
    $("#userForm input[name='roles']:checked").each(function() {
        formData.roleIds.push($(this).val());
    });

    // Отправляем данные на сервер
    try {
        await fetch('http://localhost:8080/admin/api/createUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(formData)
        });

        // После успешного создания пользователя вызываем функцию для обновления таблицы
        await createUpdateTable();
    } catch (error) {
        console.error('Произошла ошибка при создании пользователя:', error);
    }
}
