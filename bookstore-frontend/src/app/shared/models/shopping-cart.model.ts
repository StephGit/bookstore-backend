import {ShoppingCartItem} from './shopping-cart-item.model';
import {Book} from './book.model';

export class ShoppingCart {

  private items: ShoppingCartItem[] = [];

  public getItems(): ShoppingCartItem[] {
    return this.items;
  }

  public addBook(book: Book, quantity: number) {
    this.items.push(new ShoppingCartItem(book, quantity));
  }

  public removeBook(pos: number) {
    this.items.slice(pos);
  }


}

