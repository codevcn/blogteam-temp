const initPage = (data) => {
    // Dữ liệu của bạn
    const { totalOfUsers, totalOfPosts } = data

    // Cấu hình biểu đồ
    const ctx = document.getElementById("admin-bar-chart").getContext("2d")
    const chart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["Users", "Posts"],
            datasets: [
                {
                    label: "Number",
                    data: [totalOfUsers, totalOfPosts],
                    backgroundColor: ["rgba(75, 192, 192, 0.2)", "rgba(153, 102, 255, 0.2)"],
                    borderColor: ["rgba(75, 192, 192, 1)", "rgba(153, 102, 255, 1)"],
                    borderWidth: 1
                }
            ]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    })

    return chart
}

const chart = initPage({ totalOfUsers: 21, totalOfPosts: 51 })
