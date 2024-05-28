const form = document.querySelector(".form");
const fullNameInput = form.querySelector("input[name='fullName']");
const emailInput = form.querySelector("input[name='email']");
const passwordInput = form.querySelector("input[name='password']");
const retypePasswordInput = form.querySelector("input[name='retypePassword']");
const fullNameMessage = form.querySelector(".form-group.full-name").querySelector(".message");
const emailMessage = form.querySelector(".form-group.email").querySelector(".message");
const passwordMessage = form.querySelector(".form-group.password").querySelector(".message");
const retypePasswordMessage = form.querySelector(".form-group.retype-password").querySelector(".message");

const check_required_inputs = ({ fullName, email, password, retypePassword }) => {
    if (
        fullName.trim() === "" ||
        email.trim() === "" ||
        password.trim() === "" ||
        retypePassword.trim() === ""
    ) {
        Toastify.error({
            title: "Đăng ký thất bại",
            html: 'Vui lòng không bỏ trống bất kì trường nào. Các trường với <span class="SweetAlert2-required-dot-description">*</span> là bắt buộc!',
        });
        return false;
    }
    return true;
};

const check_fullName_pattern = (fullName) => {
    if (fullName_pattern.test(fullName)) {
        return true;
    }
    Toastify.error({
        title: "Đăng ký thất bại",
        msg: "Vui lòng nhập đúng định dạng họ tên!",
    });
    fullNameMessage.innerHTML = "Trường tên đầy đủ phải nằm trong khoảng từ 3 đến 30 kí tự.";
    return false;
};

const check_email_pattern = (email) => {
    if (email_pattern.test(email)) {
        return true;
    }
    Toastify.error({
        title: "Đăng ký thất bại",
        msg: "Vui lòng nhập đúng định dạng email!",
    });
    emailMessage.innerHTML = "Trường email không hợp lệ.";
    return false;
};

const check_password_pattern = (password) => {
    if (password_pattern.test(password)) {
        return true;
    }
    Toastify.error({
        title: "Đăng ký thất bại",
        msg: "Vui lòng nhập đúng định dạng mật khẩu!",
    });
    passwordMessage.innerHTML = "Mật khẩu phải từ 4 kí tự, bao gồm ít nhất 1 chữ cái và 1 số.";
    return false;
};

const check_retype_password_matchs = ({ password, retypePassword }) => {
    if (password === retypePassword) {
        return true;
    }
    Toastify.error({
        title: "Đăng ký thất bại",
        msg: "Mật khẩu nhập lại không khớp!",
    });
    retypePasswordMessage.innerHTML = "Mật khẩu nhập lại không khớp.";
    return false;
};

const validate_form = () => {
    const fullName = fullNameInput.value;
    const email = emailInput.value;
    const password = passwordInput.value;
    const retypePassword = retypePasswordInput.value;

    return (
        // Kiểm tra xem các trường có được điền đầy đủ không
        check_required_inputs({ fullName, email, password, retypePassword }) &&
        // Kiểm tra định dạng tên đầy đủ
        check_fullName_pattern(fullName) &&
        // Kiểm tra định dạng email
        check_email_pattern(email) &&
        // Kiểm tra độ dài mật khẩu
        check_password_pattern(password) &&
        // Kiểm tra độ dài mật khẩu
        check_retype_password_matchs({ password, retypePassword })
    );
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

const clearMessage = (target) => {
    target.closest(".form-group").querySelector(".message").innerHTML = "";
};

const auto_fill_form = (is_fill) => {
    if (is_fill) {
        form.querySelector("input[name='fullName']").value = "alan mog 123";
        form.querySelector("input[name='email']").value = "vivi@gim.me";
        form.querySelector("input[name='password']").value = "wibu123";
        form.querySelector("input[name='retypePassword']").value = "wibu123";
        form.querySelector("select[name='gender']").value = "male";
    } else {
        form.querySelector("input[name='fullName']").value = "";
        form.querySelector("input[name='email']").value = "";
        form.querySelector("input[name='password']").value = "";
        form.querySelector("input[name='retypePassword']").value = "";
        form.querySelector("select[name='gender']").value = "";
    }
};
