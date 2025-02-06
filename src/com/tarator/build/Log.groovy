package com.tarator.build

class Log {
    static def verbose(title, body) {
        return """
-------------------------------
${title}


${body.stripIndent().trim()}


-------------------------------
"""
    }
}