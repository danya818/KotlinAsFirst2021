@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence", "NAME_SHADOWING")

package lesson7.task1

import java.io.File
import java.lang.Integer.max

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    val newList = substrings.toSet()
    for (str in newList) map[str] = 0
    for (line in File(inputName).readLines())
        for (str in newList) {
            var searchIndex = 0
            val lowerString = line.lowercase()
            val strFind = str.lowercase()
            var ind = lowerString.indexOf(strFind, searchIndex)
            while (ind != -1) {
                map[str] = map[str]!! + 1
                searchIndex = ind + 1
                ind = lowerString.indexOf(strFind, searchIndex)
            }
        }
    return map
}


/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val set = setOf('ж', 'ч', 'ш', 'щ')
    val map = mapOf('ы' to "и", 'я' to "а", 'ю' to "у")
    File(outputName).bufferedWriter().use {
        for (line in File(inputName).readLines()) {
            it.write(line[0].toString())
            for (i in 1 until line.length)
                if ((line[i - 1].lowercaseChar() in set) && (line[i].lowercaseChar() in map))
                    it.write(
                        map.getOrDefault(
                            line[i],
                            map.getOrDefault(line[i].lowercaseChar(), "").uppercase()
                        )
                    )
                else it.write(line[i].toString())
            it.newLine()
        }
    }
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val text = mutableListOf<String>()
    var maxLen = 0
    for (line in File(inputName).readLines()) {
        val newLine = line.trim()
        text.add(newLine)
        maxLen = max(maxLen, newLine.length)
    }
    File(outputName).bufferedWriter().use {
        for (line in text) {
            val currentLen = line.length
            val res = String.format("%${(maxLen + currentLen) / 2}s", line) + "\n"
            it.write(res)
            print(res)
        }
    }
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val text = mutableListOf<List<String>>()
    val lenghtLines = mutableListOf<Int>()
    var maxLen = 0
    for (line in File(inputName).readLines()) {
        val res = line.trim().split(" ").filter { it != "" }
        var lenWords = res.fold(0) { preview, it -> preview + it.length }
        if (lenWords != 0) lenWords += (res.size - 1)
        lenghtLines.add(lenWords)
        maxLen = max(maxLen, lenWords)
        if (res.isEmpty()) text.add(listOf("")) else text.add(res)
    }
    File(outputName).bufferedWriter().use {
        for ((index, line) in text.withIndex()) {
            if (line.size == 1) {
                it.write(line[0] + "\n")
                continue
            }
            val dif = maxLen - lenghtLines[index]
            val countPosSpaces = line.size - 1
            val divSpaces = dif / countPosSpaces
            var modSpaces = dif % countPosSpaces
            for (i in 0..line.size - 2) {
                it.write(line[i] + " ".repeat(divSpaces + 1 + if (modSpaces > 0) 1 else 0))
                modSpaces--
            }
            it.write(line.last() + "\n")
        }
    }

}

    /**
     * Средняя (14 баллов)
     *
     * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
     *
     * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
     * Если в тексте менее 20 различных слов, вернуть все слова.
     * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
     * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
     *
     * Словом считается непрерывная последовательность из букв (кириллических,
     * либо латинских, без знаков препинания и цифр).
     * Цифры, пробелы, знаки препинания считаются разделителями слов:
     * Привет, привет42, привет!!! -привет?!
     * ^ В этой строчке слово привет встречается 4 раза.
     *
     * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
     * Ключи в ассоциативном массиве должны быть в нижнем регистре.
     *
     */
    fun top20Words(inputName: String): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        for (line in File(inputName).readLines()) {
            val words = Regex("""[^a-zA-zа-яА-ЯёЁ]+""").split(line).map { it.lowercase() }.filter { it.isNotEmpty() }
            for (i in words) map[i] = map.getOrDefault(i, 0) + 1
        }
        return map.toList().sortedByDescending { it.second }.take(20).toMap()
    }

    /**
     * Средняя (14 баллов)
     *
     * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

     * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
     *
     * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
     * ставится в соответствие строчка из символов, например
     * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
     *
     * Необходимо вывести в итоговый файл с именем outputName
     * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
     *
     * При этом регистр символов в словаре должен игнорироваться,
     * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
     *
     * Пример.
     * Входной текст: Здравствуй, мир!
     *
     * заменяется на
     *
     * Выходной текст: Zzdrавствуy, mир!!!
     *
     * Пример 2.
     *
     * Входной текст: Здравствуй, мир!
     * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
     *
     * заменяется на
     *
     * Выходной текст: Zzdrавствуy, mир!!!
     *
     * Обратите внимание: данная функция не имеет возвращаемого значения
     */
    fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
        val newDictionary = mutableMapOf<Char, String>()
        for ((key, value) in dictionary) newDictionary[key.lowercaseChar()] = value.lowercase()
        File(outputName).bufferedWriter().use {
            for (char in File(inputName).readText()) {
                val changeString = newDictionary[char.lowercaseChar()] ?: char.toString()
                it.write(if (char.isUpperCase()) changeString.lowercase() else changeString)
            }
        }
    }

    /**
     * Средняя (12 баллов)
     *
     * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
     * Выбрать из данного словаря наиболее длинное слово,
     * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
     * Вывести его в выходной файл с именем outputName.
     * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
     * в выходной файл следует вывести их все через запятую.
     * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
     *
     * Пример входного файла:
     * Карминовый
     * Боязливый
     * Некрасивый
     * Остроумный
     * БелогЛазый
     * ФиолетОвый

     * Соответствующий выходной файл:
     * Карминовый, Некрасивый
     *
     * Обратите внимание: данная функция не имеет возвращаемого значения
     */
    fun chooseLongestChaoticWord(inputName: String, outputName: String) {}



        /**
     * Сложная (22 балла)
     *
     * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
     *
     * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
     * - *текст в курсивном начертании* -- курсив
     * - **текст в полужирном начертании** -- полужирный
     * - ~~зачёркнутый текст~~ -- зачёркивание
     *
     * Следует вывести в выходной файл этот же текст в формате HTML:
     * - <i>текст в курсивном начертании</i>
     * - <b>текст в полужирном начертании</b>
     * - <s>зачёркнутый текст</s>
     *
     * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
     * а весь текст целиком в теги <html><body>...</body></html>.
     *
     * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
     * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
     * и никак иначе.
     *
     * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
     *
     * Пример входного файла:
    Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
    Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

    Suspendisse ~~et elit in enim tempus iaculis~~.
     *
     * Соответствующий выходной файл:
    <html>
    <body>
    <p>
    Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
    Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
    </p>
    <p>
    Suspendisse <s>et elit in enim tempus iaculis</s>.
    </p>
    </body>
    </html>
     *
     * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
     */
    fun markdownToHtmlSimple(inputName: String, outputName: String) {
        val text: String = File(inputName).readText().replace("\r", "").trim('\n')
        val textList = mutableListOf("<html><body>", "<p>")
        val map = mutableMapOf("**" to null, "*" to null, "~~" to null, "\n\n" to 1)
        var indexOfBeginString = 0
        var i = 0
        fun checkMarkToHTML(mark: String, tags: Pair<String, String>): Boolean {
            if (text[i] == mark[0]) {
                var di = i
                if (mark.length == 2) if (di < text.length - 1 && text[di + 1] == mark[1]) di += 1 else return false
                if (mark == "\n\n") while (di < text.length - 1 && text[di + 1] == '\n') di += 1
                if (i - indexOfBeginString != 0) textList.add(text.substring(indexOfBeginString, i))
                i = di
                indexOfBeginString = i + 1
                if (map[mark] == null) {
                    map[mark] = textList.size
                    textList.add(mark)
                } else {
                    textList.add(tags.second)
                    textList[map[mark]!!] = tags.first
                    map[mark] = null
                    if (mark == "\n\n") {
                        map[mark] = textList.size
                        textList.add("\n\n")
                    }
                }
                return true
            }
            return false
        }
        while (i < text.length) {
            var flag = checkMarkToHTML("**", "<b>" to "</b>")
            if (!flag) flag = checkMarkToHTML("*", "<i>" to "</i>")
            if (!flag) flag = checkMarkToHTML("~~", "<s>" to "</s>")
            if (!flag) checkMarkToHTML("\n\n", "<p>" to "</p>")
            i += 1
        }
        textList.add(text.substring(indexOfBeginString, i))
        val lastP = map["\n\n"]
        if (lastP != null) {
            textList[lastP] = "<p>"
            textList.add("</p>")
        }
        textList.add("</body></html>")
        val res = textList.joinToString(separator = "")
        File(outputName).bufferedWriter().use { it.write(res) }
    }



