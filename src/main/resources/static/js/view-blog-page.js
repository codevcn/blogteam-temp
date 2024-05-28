const pageMain = document.querySelector("#page-main")
const blogSection = pageMain.querySelector(".blog-section")
const interactions = blogSection.querySelector(".interactions")
const likesCount = interactions.querySelector(".likes-count")
const reviewsCount = interactions.querySelector(".reviews-count")
const likePostBtn = likesCount.querySelector(".like-post-btn")
const reviewSection = blogSection.querySelector(".blog-viewing-section .review-section")
const commentList = reviewSection.querySelector(".comments-list")

const setUIOfLikePostBtn = (type) => {
    if (type === "unliked") {
        likePostBtn.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-star" viewBox="0 0 16 16">
                <path d="M2.866 14.85c-.078.444.36.791.746.593l4.39-2.256 4.389 2.256c.386.198.824-.149.746-.592l-.83-4.73 3.522-3.356c.33-.314.16-.888-.282-.95l-4.898-.696L8.465.792a.513.513 0 0 0-.927 0L5.354 5.12l-4.898.696c-.441.062-.612.636-.283.95l3.523 3.356-.83 4.73zm4.905-2.767-3.686 1.894.694-3.957a.56.56 0 0 0-.163-.505L1.71 6.745l4.052-.576a.53.53 0 0 0 .393-.288L8 2.223l1.847 3.658a.53.53 0 0 0 .393.288l4.052.575-2.906 2.77a.56.56 0 0 0-.163.506l.694 3.957-3.686-1.894a.5.5 0 0 0-.461 0z" />
            </svg>`
        likePostBtn.setAttribute("data-blog-liked", "false")
    } else {
        likePostBtn.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-star-fill" viewBox="0 0 16 16">
                <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
            </svg>`
        likePostBtn.setAttribute("data-blog-liked", "true")
    }
}

const changeLikesCount = (amountOfChange) => {
    const likesCountDisplay = likesCount.querySelector(".likes-count-display")
    const attribute = "data-blog-likesCount"
    const count = likesCountDisplay.getAttribute(attribute)
    const amountAfterChange = count * 1 + amountOfChange
    likesCountDisplay.setAttribute(attribute, amountAfterChange)
    likesCountDisplay.innerHTML = amountAfterChange
}

const likePost = async (postId) => {
    await axios.post(`${like_post_api}/${postId}`)
}

const cancelLikePost = async (postId) => {
    await axios.delete(`${cancel_like_post_api}/${postId}`)
}

const likePostHandler = async (target) => {
    const liked = target.getAttribute("data-blog-liked")
    const postId = routeParameterOfURLGetter()
    target.classList.add("on-progress")
    if (liked === "true") {
        let success = false
        try {
            await cancelLikePost(postId)
            success = true
        } catch (error) {
            Toastify.error({ title: "Hủy thích bài đăng thất bại", msg: error.message })
        }
        if (success) {
            setUIOfLikePostBtn("unliked")
            changeLikesCount(-1)
        }
    } else {
        let success = false
        try {
            await likePost(postId)
            success = true
        } catch (error) {
            Toastify.error({ title: "Thích bài đăng thất bại", msg: error.message })
        }
        if (success) {
            setUIOfLikePostBtn("liked")
            changeLikesCount(1)
        }
    }
    target.classList.remove("on-progress")
}

const validateComment = (comment) => {
    let valid = true
    if (!comment) {
        valid = false
        Toastify.error({ title: "Bình luận thất bại", msg: "Vui lòng nhập bình luận để tiếp tục!" })
    }
    return valid
}

const makeReview = async (comment) => {
    const postId = routeParameterOfURLGetter()
    return await axios.post(`${make_review_api}/${postId}`, { comment })
}

const renderNewReview = (review) => {
    const { user, comment, createdAt } = review
    const commentItemCard = document.createElement("div")
    commentItemCard.classList.add("comment-item-card")

    const userAvatar = document.createElement("img")
    userAvatar.classList.add("user-avatar")
    const avatarOfUser = user.avatar
    userAvatar.src = avatarOfUser || "/imgs/user-avatar-deafult.png"

    const commentBox = document.createElement("div")
    commentBox.classList.add("comment-box")
    commentBox.innerHTML = `
        <div class="name-of-user-box">
            <span class="name-of-user">${user.fullName}</span>
            <span class="dash">-</span>
            <span class="name-of-user">${createdAt}</span>
        </div>
        <div class="comment-of-user">${comment}</div>`

    commentItemCard.appendChild(userAvatar)
    commentItemCard.appendChild(commentBox)

    commentList.prepend(commentItemCard)
}

const setUIOfReviewsCount = (amount) => {
    const countDisplay = reviewsCount.querySelector(".reviews-count-display")
    const count = countDisplay.getAttribute("data-blog-reviewsCount")
    countDisplay.innerHTML = count * 1 + amount
}

const setUIOfCommentSection = () => {
    const commentEditorSection = reviewSection.querySelector(".comment-editor-section")
    const madeReview = document.createElement("section")
    madeReview.classList.add("made-review")
    madeReview.innerHTML = `<h2>Bạn đã bình luận.</h2>`
    reviewSection.replaceChild(madeReview, commentEditorSection)
}

const makeReviewHandler = async (target) => {
    const commentEditor = target.closest(".comment-editor-section").querySelector(".comment-editor")
    const comment = commentEditor.value
    if (validateComment(comment)) {
        commentEditor.value = ""
        target.innerHTML = getHTMLSpinner()
        let success = false
        let result
        try {
            const { data } = await makeReview(comment)
            console.log(">>> data >>>", data)
            result = data
            success = true
        } catch (error) {
            Toastify.error({ title: "Bình luận thất bại", msg: error.message })
        }
        if (success) {
            renderNewReview(result)
            setUIOfCommentSection()
            setUIOfReviewsCount(1)
        }
        target.innerHTML = `<span>Gửi bình luận</span>`
    }
}
