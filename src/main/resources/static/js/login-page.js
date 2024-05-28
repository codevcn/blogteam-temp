const form = document.querySelector(".form");
const emailInput = form.querySelector("input[name='email']");
const passwordInput = form.querySelector("input[name='password']");

const check_required_inputs = ({ email, password }) => {
    if (email.trim() === "" || password.trim() === "") {
        Toastify.error({
            title: "Đăng ký thất bại",
            html: "Vui lòng không bỏ trống bất kì trường nào!",
        });
        return false;
    }
    return true;
};

const validate_form = () => {
    const email = emailInput.value;
    const password = passwordInput.value;

    return check_required_inputs({ email, password });
};

const hide_show_password = (is_shown) => (target) => {
    const sibling_input = target.parentElement.querySelector("input");
    if (is_shown) {
        sibling_input.type = "text";
        target.style.display = "none";
        target.nextElementSibling.style.display = "block";
    } else {
        sibling_input.type = "password";
        target.style.display = "none";
        target.previousElementSibling.style.display = "block";
    }
};

const auto_fill_form = (is_fill) => {
    if (is_fill) {
        form.querySelector("input[name='email']").value = "vivi@gim.me";
        form.querySelector("input[name='password']").value = "wibu123";
    } else {
        form.querySelector("input[name='email']").value = "";
        form.querySelector("input[name='password']").value = "";
    }
};
