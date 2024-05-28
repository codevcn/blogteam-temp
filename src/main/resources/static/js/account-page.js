const pageMain = document.querySelector("#page-main")
const navAndMainContent = pageMain.querySelector(".nav-and-main-content")
const userNavSideBar = navAndMainContent.querySelector(".user-nav-side-bar")

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
