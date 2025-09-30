import kotlin.math.abs

class PhanSo(var tuSo: Int = 0, var mauSo: Int = 1) {

    fun nhapPhanSo() {
        print("Nhập vào tử số: ")
        this.tuSo = readIntegerInput("Nhập vào tử số: ")
        do {
            this.mauSo = readIntegerInput("Nhập vào mẫu số (khác 0): ")
            if (this.mauSo == 0) {
                println("Mẫu số phải khác 0. Vui lòng nhập lại.")
            }
        } while (this.mauSo == 0)
    }

    override fun toString(): String {
        return "$tuSo/$mauSo"
    }

    // Tim UCLN de toi gian phan so
    private fun timUCLN(a: Int, b: Int): Int {
        return if (b == 0) a else timUCLN(b, a % b)
    }

    //toi gian
    fun toiGian() {
        val ucln = timUCLN(abs(tuSo), abs(mauSo))
        this.tuSo /= ucln
        this.mauSo /= ucln
    }

    // Tong cac phan so (neu toi gian duoc se toi gian)
    operator fun plus(other: PhanSo): PhanSo {
        val tongTuSo = this.tuSo * other.mauSo + other.tuSo * this.mauSo
        val tongMauSo = this.mauSo * other.mauSo
        val ketQua = PhanSo(tongTuSo, tongMauSo)
        ketQua.toiGian()
        return ketQua
    }

    // So sanh voi phan so khac
    fun soSanh(other: PhanSo): Int {
        val res = this.tuSo * other.mauSo - other.tuSo * this.mauSo
        return when {
            res > 0 -> 1
            res < 0 -> -1
            else -> 0
        }
    }
}