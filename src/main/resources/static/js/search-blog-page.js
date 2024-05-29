const pageMain = document.querySelector("#page-main")
const searchBar = pageMain.querySelector(".search-bar")
const searchStatus = searchBar.querySelector(".search-status")
const searchInputWrapper = searchBar.querySelector(".input-wrapper")
const searchResultList = pageMain.querySelector(".result-list-section .result-list")
const paginationBox = pageMain.querySelector(".result-list-section .pagination-box")

const visitPostHandler = (href, postId) => async () => {
    const pageProgress = document.getElementById("page-progress")
    pageProgress.innerHTML = getHTMLSpinner()
    try {
        await axios.post(`${visit_blog_api}/${postId}`)
    } catch (error) {
        const err = APIErrorHandler.handleError(error)
    }
    pageProgress.innerHTML = ""
    window.location.href = href
}

const renderSearchResult = (post) => {
    const user = post.user

    const postCard = document.createElement("div")
    postCard.classList.add("post-card")

    const userSection = document.createElement("div")
    userSection.classList.add("user-section")

    const userAvatar = document.createElement("img")
    userAvatar.classList.add("user-avatar")
    if (user.avatar) {
        userAvatar.src = user.avatar
    } else {
        userAvatar.src = "/imgs/user-avatar-deafult.png"
    }

    const userInfo = document.createElement("div")
    userInfo.classList.add("user-info")
    userInfo.innerHTML = `
        <div class="name-of-user">${user.fullName}</div>
        <div class="date-of-post">${dayjs(post.createdAt).format("DD/MM/YYYY HH:mm")}</div>`

    userSection.appendChild(userAvatar)
    userSection.appendChild(userInfo)

    const postTitle = document.createElement("div")
    postTitle.classList.add("post-title")
    postTitle.textContent = post.title
    const postId = post.id
    postTitle.addEventListener("click", visitPostHandler("/blog/view-blog/" + postId, postId))

    const hashtag = document.createElement("div")
    hashtag.classList.add("hashtag")
    hashtag.textContent = `#${post.hashtag}`

    const commemtSection = document.createElement("div")
    commemtSection.classList.add("comment-section")

    const commentAndLikeCounts = document.createElement("div")
    commentAndLikeCounts.classList.add("comment-and-like-counts")

    const commentCount = document.createElement("div")
    commentCount.classList.add("comments-count-box")
    commentCount.setAttribute("title", "Lượt bình luận")
    commentCount.innerHTML = `
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chat" viewBox="0 0 16 16">
            <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"/>
        </svg>
        <span class="comments-count">${post.reviewsCount || 0}</span>`

    const likesCount = document.createElement("div")
    likesCount.classList.add("likes-count-box")
    likesCount.setAttribute("title", "Lượt thích")
    likesCount.innerHTML = `
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-heart" viewBox="0 0 16 16">
            <path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15"/>
        </svg>
        <span class="likes-count">${post.likesCount || 0}</span>`

    commentAndLikeCounts.appendChild(commentCount)
    commentAndLikeCounts.appendChild(likesCount)

    const placeholder = document.createElement("span")

    commemtSection.appendChild(placeholder)
    commemtSection.appendChild(commentAndLikeCounts)

    postCard.appendChild(userSection)
    postCard.appendChild(postTitle)
    postCard.appendChild(hashtag)
    postCard.appendChild(commemtSection)

    return postCard
}

const paginationData = { current: 1 }
const paginateHandler = (page) => async (e) => {
    if (page === paginationData.current) return
    let page_for_pagination = page
    if (page === "pre") {
        page_for_pagination = paginationData.current - 1
    } else if (page === "next") {
        page_for_pagination = paginationData.current + 1
    }
    const inputValue = searchInputWrapper.querySelector("input").value
    let apiSuccess = false
    let apiResult
    try {
        const { data } = await searchRequest(inputValue, page_for_pagination)
        console.log(">>> search with pagination >>>", data)
        apiResult = data
        apiSuccess = true
    } catch (error) {
        const err = APIErrorHandler.handleError(error)
        Toastify.error({ title: "Tìm kiếm thất bại", msg: err.message })
    }
    if (apiSuccess) {
        paginationData.current = page_for_pagination
        const { posts, pagesCount } = apiResult
        if (posts && posts.length > 0) {
            setSearchBlogMessage()
            renderSearchResultList(posts)
        } else {
            setSearchBlogMessage("not-found", inputValue)
        }
        createPagination(pagesCount, page_for_pagination)
    }
    setSearchStatus()
}

const clearUIOfPagination = () => {
    paginationBox.innerHTML = ""
}

const createPagination = (pagesCount, activePage) => {
    paginationBox.innerHTML = ""
    paginationBox.appendChild(createHTMLPagination(pagesCount, activePage, paginateHandler))
}

const renderSearchResultList = async (list) => {
    const resultListTitle = document.createElement("div")
    resultListTitle.classList.add("result-list-title")
    resultListTitle.textContent = "Kết quả:"

    searchResultList.appendChild(resultListTitle)

    for (const post of list) {
        const postCard = renderSearchResult(post)
        searchResultList.appendChild(postCard)
    }
}

const setSearchBlogMessage = (type, keyword) => {
    if (type) {
        if (type === "not-found") {
            searchResultList.innerHTML = `
            <div class="result-message">Không tìm thấy bất cứ bài đăng nào với "${keyword}".</div>`
        }
    } else {
        searchResultList.innerHTML = ""
    }
}

const validateInputValue = (inputValue) => {
    let valid = true
    if (!inputValue) {
        valid = false
        setSearchStatus("message", "Vui lòng không để trống trường tìm kiếm")
    }
    return valid
}

const setSearchStatus = (type, message) => {
    if (type) {
        if (type === "on-progress") {
            searchStatus.innerHTML = `
                <div class="spinner-border spinner" role="status">
                    <span class="sr-only"></span>
                </div>`
        } else if (type === "message") {
            searchStatus.innerHTML = `
                <div class="message invalid" role="status">
                    <span>${message}</span>
                </div>`
        }
    } else {
        searchStatus.innerHTML = ""
    }
}

const searchRequest = async (input_value, page) => {
    return await axios.get(`${search_blog_api}?keyword=${input_value}&page=${page}`)
}

const fetchResultSearchHandler = debounce(async (inputValue) => {
    if (validateInputValue(inputValue)) {
        setSearchStatus("on-progress")
        let apiSuccess = false
        let apiResult
        try {
            const { data } = await searchRequest(inputValue, 1)
            console.log(">>> normal search data >>>", data)
            apiResult = data
            apiSuccess = true
        } catch (error) {
            Toastify.error({ title: "Tìm kiếm thất bại", msg: error.message })
        }
        if (apiSuccess) {
            const { posts, pagesCount } = apiResult
            if (posts && posts.length > 0) {
                setSearchBlogMessage()
                renderSearchResultList(posts)
                createPagination(pagesCount, 1)
            } else {
                setSearchBlogMessage("not-found", inputValue)
            }
        }
        setSearchStatus()
    }
}, 500)

const searchBlog = () => {
    const inputValue = searchInputWrapper.querySelector("input").value
    if (inputValue) {
        fetchResultSearchHandler(inputValue)
    } else {
        setSearchBlogMessage()
        clearUIOfPagination()
    }
}
