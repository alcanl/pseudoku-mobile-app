package com.alcanl.sudoku.service

class ServiceException(override val cause: Throwable?) : Exception() {
}