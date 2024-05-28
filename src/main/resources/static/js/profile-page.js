const pageMain = document.querySelector("#page-main")
const profileSection = pageMain.querySelector(".profile-section")
const personalInfoSection = profileSection.querySelector(".profile-container.personal-info-section")
const passwordSection = profileSection.querySelector(".profile-container.password-section")
const newPasswordMessage = passwordSection.querySelector(".form-group.new-password .message")
const oldPasswordMessage = passwordSection.querySelector(".form-group.old-password .message")

const logoutUser = async () => {
    await axios.post(logout_user_api)
}

const logoutUserHandler = async (target) => {
    Toastify.confirm({ title: "Xác nhận đăng xuất", msg: "Bạn có chắc muốn đăng xuất" }, async () => {
        target.innerHTML = `
            <div class="spinner-border spinner" role="status">
                <span class="sr-only"></span>
            </div>`
        let success = false
        try {
            await logoutUser()
            success = true
        } catch (error) {
            Toastify.error({ title: "Đăng xuất thất bại", msg: error.message })
        }
        if (success) {
            setTimeout(() => {
                window.location.href = "/"
            }, 500)
        }
        target.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-box-arrow-right"
                viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                    d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z" />
                <path fill-rule="evenodd"
                    d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z" />
            </svg>
            <span>Đăng xuất</span>`
    })
}

const hide_show_password = (target, is_shown) => {
    const sibling_input = target.closest(".input-wrapper").querySelector("input")
    if (is_shown) {
        sibling_input.type = "text"
        target.hidden = true
        target.nextElementSibling.hidden = false
    } else {
        sibling_input.type = "password"
        target.hidden = true
        target.previousElementSibling.hidden = false
    }
}

const initPage = () => {
    if (error) {
        Toastify.error({
            title: "Đăng nhập thất bại",
            msg: message
        })
    } else if (success) {
        Toastify.success({ title: "Cập nhật thành công", msg: message })
    }
}
initPage()

const setNewPasswordMessage = (message) => {
    newPasswordMessage.innerHTML = message || ""
}

const setOldPasswordMessage = (message) => {
    oldPasswordMessage.innerHTML = message || ""
}

const validatePassword = (oldPassword, newPassword) => {
    let valid = true
    if (password_pattern.test(newPassword)) {
        setNewPasswordMessage()
    } else {
        valid = false
        setNewPasswordMessage("Mật khẩu phải từ 4 kí tự, bao gồm ít nhất 1 chữ cái và 1 số!")
    }
    if (oldPassword) {
        setNewPasswordMessage()
    } else {
        valid = false
        setNewPasswordMessage("Vui lòng không để trống mật khẩu!")
    }
    return valid
}

const updatePasswordHandler = async (target) => {
    const oldPassword = passwordSection.querySelector(".form-group.old-password .input-wrapper input").value
    const newPassword = passwordSection.querySelector(".form-group.new-password .input-wrapper input").value
    if (validatePassword(oldPassword, newPassword)) {
        target.innerHTML = getHTMLSpinner()
        let apiSucceeds = false
        try {
            await axios.put(update_password_api, { oldPassword, newPassword })
            apiSucceeds = true
        } catch (error) {
            const err = APIErrorHandler.handleError(error)
            Toastify.error({ title: "Cập nhật mật khẩu thất bại", msg: err.message })
        }
        if (apiSucceeds) {
            Toastify.success({ title: "Cập nhật mật khẩu thành công", msg: "Cập nhật mật khẩu thành công" })
        }
        target.innerHTML = `<span>Lưu thay đổi</span>`
    }
}
