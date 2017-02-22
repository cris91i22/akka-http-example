package com.akka.http.dl.storage

import com.akka.http.model.{Message, User}

import scala.concurrent.Future

trait Storage {
  def users: BasicObjectStorage[User]
  def messages: BasicObjectStorage[Message]

  def countUsers: StorageQuery0[Int]
  def retrieveUsersWithMessages: StorageQuery0[Map[User, Seq[Message]]]

}

trait BasicObjectStorage[T] {
  def retrieve(id: Int): Future[Option[T]]
  def update(obj: T): Future[Int]
  def create(obj: T): Future[T]
  def delete(id: Int): Future[Int]
}

trait StorageQuery0[R] {
  def run(): Future[R]
}

trait StorageQuery1[A, R] {
  def run(a: A): Future[R]
}

trait StorageQuery2[A, B, R] {
  def run(a: A, b: B): Future[R]
}

trait StorageQuery3[A, B, C, R] {
  def run(a: A, b: B, c: C): Future[R]
}

trait StorageQuery4[A, B, C, D, R] {
  def run(a: A, b: B, c: C, d: D): Future[R]
}