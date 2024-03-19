package com.alcanl.sudoku.repository

import java.lang.Exception

class RepositoryException(override val cause: Throwable) : RuntimeException()

