import {Book} from './book.model';

export class ShoppingCartItem {
  constructor(public book: Book, public quantity: number = 1) {}
}
