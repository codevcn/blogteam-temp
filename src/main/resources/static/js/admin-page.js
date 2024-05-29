const pageMain = document.querySelector("#page-main")
const metricsList = pageMain.querySelector(".metrics-section .metrics")

const renderChartUI = (data) => {
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
const chart = renderChartUI(ssrVar_metrics)

const setUIOfMetrics = (data) => {
    const { totalOfUsers, totalOfPosts } = data
    metricsList.querySelector(".total-of-users .total-value").innerHTML = `<span>${totalOfUsers}</span>`
    metricsList.querySelector(".total-of-posts .total-value").innerHTML = `<span>${totalOfPosts}</span>`
}

const initPage = async (data) => {
    setUIOfMetrics(data)
}
initPage(ssrVar_metrics)
