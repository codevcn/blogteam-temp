const queryStringGetter = (param) => {
    return new URLSearchParams(window.location.search).get(param)
}

const routeParameterOfURLGetter = (route = window.location.href) => {
    return route.split("/").pop()
}

class Toastify {
    static #confirmButtonText = "OK"
    static #cancelButtonText = "Hủy"

    static error({ title, msg, html }) {
        Swal.fire({
            title,
            ...(html ? { html } : { text: msg }),
            icon: "error",
            cancelButtonText: this.#confirmButtonText,
            cancelButtonText: this.#cancelButtonText
        })
    }
    static success({ title, msg, html }) {
        Swal.fire({
            title,
            ...(html ? { html } : { text: msg }),
            icon: "success",
            cancelButtonText: this.#confirmButtonText,
            cancelButtonText: this.#cancelButtonText
        })
    }
    static confirm({ title, msg }, callback) {
        Swal.fire({
            title,
            text: msg,
            icon: "warning",
            showCancelButton: true,
            cancelButtonText: this.#confirmButtonText,
            cancelButtonText: this.#cancelButtonText
        }).then((result) => {
            if (result.isConfirmed) {
                callback()
            }
        })
    }
}

const toastify = {
    error: ({ title, msg, html }) => {
        Swal.fire({
            title,
            ...(html ? { html } : { text: msg }),
            icon: "error",
            cancelButtonText: this.confirmButtonText
        })
    },
    success: ({ title, msg, html }) => {
        Swal.fire({
            title,
            ...(html ? { html } : { text: msg }),
            icon: "success"
        })
    },
    confirm: ({ title, msg }, callback) => {
        Swal.fire({
            title,
            text: msg,
            icon: "warning",
            showCancelButton: true
        }).then((result) => {
            if (result.isConfirmed) {
                callback()
            }
        })
    }
}

function debounce(func, wait) {
    let timeout
    return function (...args) {
        const context = this
        clearTimeout(timeout)
        timeout = setTimeout(() => func.apply(context, args), wait)
    }
}

const getHTMLSpinner = () => {
    return `
        <div class="spinner-border spinner" role="status">
            <span class="sr-only"></span>
        </div>`
}

const createHTMLPagination = (numberOfPages, activePage, func) => {
    const firstPage = activePage > MAX_PAGINATION ? activePage - MAX_PAGINATION : 1
    let finalPage = firstPage + MAX_PAGINATION - 1
    if (finalPage > numberOfPages) {
        finalPage = numberOfPages
    }

    const navEle = document.createElement("nav")
    const ulEle = document.createElement("ul")
    ulEle.classList.add("pagination")

    const liEle_first = document.createElement("li")
    liEle_first.classList.add("page-item")
    liEle_first.innerHTML = `<span class="page-link">Trước</span>`
    liEle_first.setAttribute("data-pagination-value", "pre")
    if (firstPage < activePage) {
        liEle_first.addEventListener("click", func("pre"))
    }

    ulEle.appendChild(liEle_first)

    for (let i = firstPage; i <= finalPage; i++) {
        const liEle = document.createElement("li")
        liEle.classList.add("page-item")
        if (i === activePage) {
            liEle.classList.add("active")
        }
        liEle.innerHTML = `<span class="page-link">${i}</span>`
        if (i !== activePage) {
            liEle.addEventListener("click", func(i))
        }
        liEle.setAttribute("data-pagination-value", i)

        ulEle.appendChild(liEle)
    }

    const liEle_final = document.createElement("li")
    liEle_final.classList.add("page-item")
    liEle_final.innerHTML = ` <span class="page-link">Sau</span>`
    liEle_final.setAttribute("data-pagination-value", "next")
    if (activePage < finalPage) {
        liEle_final.addEventListener("click", func("next"))
    }

    ulEle.appendChild(liEle_final)

    navEle.appendChild(ulEle)

    return navEle
}

class APIErrorHandler {
    static handleError(originalError, defaultMessage = "Data requirement failed...") {
        const error = {
            httpStatus: 500,
            isUserError: false,
            message: defaultMessage,
            originalError
        }
        const response_of_error = originalError.response
        //if error was made by server at backend
        if (response_of_error) {
            error.httpStatus = response_of_error.status //update error status
            const data_of_response = response_of_error.data
            if (typeof data_of_response === "string") {
                error.isUserError = false
                error.message = "Invalid request"
            } else {
                error.isUserError = data_of_response.isUserException //check if is error due to user or not
                error.message = data_of_response.message //update error message

                if (error.message.length > MAX_LENGTH_OF_API_ERROR_MESSAGE) {
                    error.message = `${error.message.slice(0, MAX_LENGTH_OF_API_ERROR_MESSAGE)}...`
                }
            }
        } else if (error.originalError.request) {
            //The request was made but no response was received
            error.httpStatus = 502
            error.message = "Bad network or error"
        } else {
            //Something happened in setting up the request that triggered an Error
        }
        return error
    }
}

const redirectAfterMs = (href, timing) => {
    setTimeout(() => {
        window.location.href = href
    }, timing)
}
