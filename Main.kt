/** Prints the receiver argument to the standard output stream. */
fun <T> T.printlnIt() = println(this)

/** Prints the receiver argument to the standard output stream, then reads an input and returns it. */
fun <T> T.reply() = this.printlnIt().run { readln() }

fun String.encoded() = this
    .toCharArray()
    .joinToString("") { it.code.toString(2).padStart(7, '0') }
    .replace("01", "0 1")
    .replace("10", "1 0")
    .split(" ")
    .joinToString(" ") { "${if (it.first() == '0') "00" else "0"} ${"0".repeat(it.length)}" }

fun String.decoded() = this
    .split(" ")
    .chunked(2)
    .takeIf { stringList -> stringList.all { it.joinToString(" ").matches(Regex("0{1,2} 0+")) } }
    ?.joinToString("") { (if (it[0] == "0") "1" else "0").repeat(it[1].length) }
    ?.takeIf { it.length % 7 == 0 }
    ?.chunked(7)
    ?.joinToString("") { it.toInt(2).toChar().toString() }
    ?: throw IllegalArgumentException("Encoded message not valid.")

fun main() {
    while (true) {
        when (val operation = "Please input operation (encode/decode/exit):".reply()) {
            "encode" -> "Input string:".reply().run { "Encoded string:\n${this.encoded()}" }.printlnIt()
            "decode" -> "Input encoded string:".reply()
                .runCatching { "Decoded string:\n${this.decoded()}".printlnIt() }
                .onFailure { "Encoded message not valid.".printlnIt() }
            "exit" -> break
            else -> "There is no '$operation' operation".printlnIt()
        }
    }
    "Bye!".printlnIt()
}
