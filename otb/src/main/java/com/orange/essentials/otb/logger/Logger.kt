package com.orange.essentials.otb.logger

import android.util.Log

/**
 * Custom logger that can be deactivated.
 */
object Logger {
    /** The depth to go in the call stack, to get interesting calling method name.  */
    private val DEPTH = 4

    /** Activation status. by default disabled.  */
    /**
     * @return true if logging is allowed, false otherwise.
     */
    var loggingAllowed = false

    /**
     * Send a VERBOSE log message.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun v(tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.v(tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun v(tag: String, msg: String, tr: Throwable): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.v(tag, updatedMsg, tr)
        }

        return bytesWritten
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun d(tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.d(tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun d(tag: String, msg: String, tr: Throwable): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.d(tag, updatedMsg + '\n'.toString() + getStackTraceString(tr))
        }

        return bytesWritten
    }

    /**
     * Send an INFO log message.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun i(tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.i(tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun i(tag: String, msg: String, tr: Throwable): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.i(tag, updatedMsg + '\n'.toString() + getStackTraceString(tr))
        }

        return bytesWritten
    }

    /**
     * Send a WARN log message.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun w(tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.w(tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun w(tag: String, msg: String, tr: Throwable): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.w(tag, updatedMsg, tr)
        }

        return bytesWritten
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun w(tag: String, tr: Throwable): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH)

        if (loggingAllowed) {
            bytesWritten = Log.w(tag, updatedMsg, tr)
        }

        return bytesWritten
    }

    /**
     * Send an ERROR log message.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun e(tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.e(tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun e(tag: String, msg: String, tr: Throwable): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.e(tag, updatedMsg, tr)
        }

        return bytesWritten
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the [android.os.DropBoxManager] and/or
     * the process may be terminated
     * immediately with an error dialog.
     *
     * @param tag
     * Used to identify the source of a log message.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun wtf(tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.wtf(tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * Like [.wtf], but also writes to the log the full
     * call stack.
     *
     * @param tag
     * Used to identify the source of a log message.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun wtfStack(tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.wtf(tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to [.wtf], with an exception to log.
     *
     * @param tag
     * Used to identify the source of a log message.
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun wtf(tag: String, tr: Throwable): Int {
        return wtf(tag, tr.message!!, tr)
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to [.wtf], with a message as well.
     *
     * @param tag
     * Used to identify the source of a log message.
     * @param msg
     * The message you would like logged.
     * @param tr
     * An exception to log. May be null.
     * @return The number of bytes written.
     */
    fun wtf(tag: String, msg: String, tr: Throwable): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.wtf(tag, updatedMsg, tr)
        }

        return bytesWritten
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable.
     *
     * @param tr
     * An exception to log.
     * @return The number of bytes written.
     */
    fun getStackTraceString(tr: Throwable): String {
        return Log.getStackTraceString(tr)
    }

    /**
     * Low-level logging call.
     *
     * @param priority
     * The priority/type of this log message
     * @param tag
     * Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg
     * The message you would like logged.
     * @return The number of bytes written.
     */
    fun println(priority: Int, tag: String, msg: String): Int {
        var bytesWritten = 0
        val updatedMsg = getMethodName(DEPTH) + "() : " + msg

        if (loggingAllowed) {
            bytesWritten = Log.println(priority, tag, updatedMsg)
        }

        return bytesWritten
    }

    /**
     * Get the method name for a provided depth in call stack.
     * @param parDepth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    private fun getMethodName(parDepth: Int): String {
        val ste = Thread.currentThread().stackTrace
        return ste[parDepth].methodName
    }
}