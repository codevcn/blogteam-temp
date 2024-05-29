const font_families_for_quilljs = ["Arial", "Roboto", "Times-New-Roman", "Poppins", "Work-Sans"]
const font_sizes_for_quilljs = ["11px", "14px", "18px", "24px", "30px"]

// init elements that usable right away
const pageMain = document.querySelector("#page-main")
const letters_counter = pageMain.querySelector(".publish-blog-container .editor-counter-container .editor-counter")
const blog_main_content = document.getElementById("quill-editor")
const blog_title = pageMain.querySelector(".editor-container .add-title") //textarea
const blog_hashtag = pageMain.querySelector(".editor-container .add-hashtag input")
const publishBlogBtn = pageMain.querySelector(".publish-blog-container .publish-blog-btn")

const quill_editor_options = {
    theme: "snow",
    placeholder: "Viết nội dung cho bài đăng của bạn...",
    modules: {
        toolbar: "#quill-toolbar",
        history: {
            delay: 1000,
            maxStack: 50,
            userOnly: false
        }
    }
}

// custom font families quill
const FontAttributor = Quill.import("attributors/class/font")
FontAttributor.whitelist = font_families_for_quilljs
Quill.register(FontAttributor, true)

// custom font sizes quill
const SizeAttributor = Quill.import("attributors/style/size")
SizeAttributor.whitelist = font_sizes_for_quilljs
Quill.register(SizeAttributor, true)

// generate quill editor
const quill = new Quill("#quill-editor", quill_editor_options)

// letters counter
const count_letters_in_editor = () => blog_main_content.querySelector(".ql-editor").innerHTML.length

quill.on(Quill.events.TEXT_CHANGE, function lettersCounter() {
    const content_length = count_letters_in_editor()
    if (content_length > max_content_letters_count) {
        if (!letters_counter.classList.contains("invalid")) {
            letters_counter.classList.add("invalid")
        }
    } else {
        letters_counter.classList.remove("invalid")
    }
    if (quill.getText().length === 1) {
        letters_counter.innerText = `0 / ${max_content_letters_count}`
    } else {
        letters_counter.innerText = `${content_length - 1} / ${max_content_letters_count}`
    }
})

// add tooltip to toolbar formats
const add_tooltip_to_buttons = (tooltip_element, hidden_class) => {
    const format_buttons = document.querySelectorAll(".editor-container .ql-toolbar.ql-snow .ql-formats button")
    if (format_buttons.length === 0) return

    for (const button of format_buttons) {
        tooltip_element.innerHTML = button.getAttribute("data-tooltip")
        button.appendChild(tooltip_element.cloneNode(true))

        button.addEventListener("mouseover", function () {
            button.querySelector(".quill-toolbar-tooltip").classList.remove(hidden_class)
        })

        button.addEventListener("mouseout", function () {
            button.querySelector(".quill-toolbar-tooltip").classList.add(hidden_class)
        })
    }
}

const add_tooltip_to_pickers = (tooltip_element, hidden_class) => {
    const format_picker_labels = document.querySelectorAll(
        ".editor-container .ql-toolbar.ql-snow .ql-formats .ql-picker-label"
    )
    if (format_picker_labels.length === 0) return

    for (const picker of format_picker_labels) {
        tooltip_element.innerHTML = picker.parentElement.getAttribute("data-tooltip")
        picker.appendChild(tooltip_element.cloneNode(true))

        picker.addEventListener("mouseover", function () {
            picker.querySelector(".quill-toolbar-tooltip").classList.remove(hidden_class)
        })

        picker.addEventListener("mouseout", function () {
            picker.querySelector(".quill-toolbar-tooltip").classList.add(hidden_class)
        })
    }
}

const add_tooltip_to_toolbar_formats = () => {
    const hidden_class = "hidden"

    const tooltip_element = document.createElement("div")
    tooltip_element.className = "quill-toolbar-tooltip " + hidden_class

    add_tooltip_to_buttons(tooltip_element, hidden_class)
    add_tooltip_to_pickers(tooltip_element, hidden_class)

    tooltip_element.remove()
}
add_tooltip_to_toolbar_formats()

// validations
const validate_post_content = () => {
    const content_length = count_letters_in_editor()
    if (content_length < min_content_letters_count || content_length > max_content_letters_count) {
        throw new Error(
            "Nội dung bài đăng phải có số lượng kí tự trong khoảng từ " +
                min_content_letters_count +
                " đến " +
                max_content_letters_count +
                " kí tự!"
        )
    }
}
const validate_post_title = () => {
    const title_length = blog_title.value.length
    if (title_length < min_title_letters_count || title_length > max_title_letters_count) {
        throw new Error(
            "Tiêu đề phải có số lượng kí tự trong khoảng từ " +
                min_title_letters_count +
                " đến " +
                max_title_letters_count +
                " kí tự!"
        )
    }
}
const validate_post_hashtag = () => {
    const hashtag_value = blog_hashtag.value
    const hashtag_length = hashtag_value.length
    if (hashtag_length < min_hashtag_letters_count || hashtag_length > max_hashtag_letters_count) {
        throw new Error(
            "Hashtag phải có số lượng kí tự trong khoảng từ " +
                min_hashtag_letters_count +
                " đến " +
                max_hashtag_letters_count +
                " kí tự!"
        )
    }
    const hashtag_regexp = /^[a-zA-Z0-9_-]+$/
    if (!hashtag_regexp.test(hashtag_value)) {
        throw new Error(
            "Hashtag không được có khoảng trắng và chỉ được chứa số, ký tự Latin, dấu gạch dưới (_) và dấu gạch ngang (-)"
        )
    }
}

const validate_blog = () => {
    validate_post_title()
    validate_post_content()
    validate_post_hashtag()
}

// request create blog
const create_blog = async (data) => {
    return await axios.post(create_blog_api, data)
}

// publish blog hanlder
const submit_publish_blog = async () => {
    try {
        validate_blog()
    } catch (error) {
        if (error !== null) {
            Toastify.error({ title: "Xuất bản bài đăng thất bại", msg: error.message })
            return
        }
    }

    const blogMainContent = blog_main_content.querySelector(".ql-editor")
    const blogTitle = blog_title.value
    const blogHashtag = blog_hashtag.value

    publishBlogBtn.innerHTML = `
        <div class="spinner-border spinner" role="status">
            <span class="sr-only"></span>
        </div>`

    try {
        await create_blog({
            title: blogTitle,
            mainContent: blogMainContent.innerHTML,
            hashtag: blogHashtag
        })
        window.location.href = "/blog/my-posts"
    } catch (error) {
        Toastify.error({ title: "Xuất bản bài đăng thất bại", msg: error.message })
        return
    }

    publishBlogBtn.innerHTML = `Xuất bản`
}
