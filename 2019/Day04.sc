def increasing(i: String): Boolean = i.toSeq.sliding(2).forall(s => s(0) <= s(1))
def hasDouble (i: String): Boolean = s" $i ".toSeq.sliding(4).exists(s => s(0) != s(1) && s(1) == s(2) && s(2) != s(3))

(125730 to 579381).map(_.toString).count(i => increasing(i) && hasDouble(i))