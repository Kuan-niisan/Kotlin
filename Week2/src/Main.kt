// readln().toInt()
fun readIntegerInput(prompt: String): Int {
    while (true) {
        print(prompt)
        try {
            return readln().toInt()
        } catch (e: NumberFormatException) {
            println("Đầu vào không hợp lệ. Vui lòng nhập một số nguyên.")
        }
    }
}


fun main() {
    val n = readIntegerInput("Nhập số lượng phân số trong mảng: ")
    val mangPhanSo = Array(n) {
        println("\nNhập phân số thứ ${it + 1}:")
        val ps = PhanSo()
        ps.nhapPhanSo()
        ps
    }

    println("\nMảng phân số vừa nhập:")
    println(mangPhanSo.joinToString("  "))

    mangPhanSo.forEach { it.toiGian() }
    println("\nMảng phân số sau khi tối giản:")
    println(mangPhanSo.joinToString("  "))

    if (mangPhanSo.isNotEmpty()) {
        val tong = mangPhanSo.reduce { acc, phanSo -> acc + phanSo }
        println("\nTổng các phân số là: $tong")
    }

    if (mangPhanSo.isNotEmpty()) {
        var max = mangPhanSo[0]
        for (i in 1 until mangPhanSo.size) {
            if (mangPhanSo[i].soSanh(max) > 0) {
                max = mangPhanSo[i]
            }
        }
        println("\nPhân số lớn nhất trong mảng là: $max")
    }

    mangPhanSo.sortWith { ps1, ps2 ->
        ps2.soSanh(ps1)
    }
    println("\nMảng sau khi sắp xếp giảm dần:")
    println(mangPhanSo.joinToString("  "))
}